package net.whxxykj.maya.app.ctrl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpService;
import net.whxxykj.maya.MayaConstant;
import net.whxxykj.maya.base.BaseConstant;
import net.whxxykj.maya.base.entity.LogOperateData;
import net.whxxykj.maya.base.entity.SysCompany;
import net.whxxykj.maya.base.entity.SysEmployee;
import net.whxxykj.maya.base.entity.SysOperator;
import net.whxxykj.maya.base.service.LogOperateDataService;
import net.whxxykj.maya.base.service.SysCompanyService;
import net.whxxykj.maya.base.service.SysEmployeeService;
import net.whxxykj.maya.base.service.SysLoginService;
import net.whxxykj.maya.base.service.SysOperatorService;
import net.whxxykj.maya.base.service.VPermissionAllotDataService;
import net.whxxykj.maya.common.ctrl.BaseCtrl;
import net.whxxykj.maya.common.entity.ManagerUser;
import net.whxxykj.maya.common.exception.BaseException;
import net.whxxykj.maya.common.kaptcha.MyKaptcha;
import net.whxxykj.maya.common.log.oper.enums.LogLevel;
import net.whxxykj.maya.common.log.oper.enums.LogType;
import net.whxxykj.maya.common.runtime.Runtime;
import net.whxxykj.maya.common.util.DateUtil;
import net.whxxykj.maya.common.util.HttpUtil;
import net.whxxykj.maya.common.util.JsonModel;
import net.whxxykj.maya.common.util.JwtUtils;
import net.whxxykj.maya.common.util.StringUtil;
import net.whxxykj.maya.plugin.cache.RedisCacheService;

/**
 * 
 * @ClassName:  MobileLoginCtrl   
 * @Description: app???????????????   
 * @author: YEJUN
 * @date:   2021???6???21??? ??????7:16:37      
 * @Copyright:
 */
@RestController
@RequestMapping("/mobile/sysm/login")
public class MobileLoginCtrl extends BaseCtrl<SysOperatorService, SysOperator> {

    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private SysEmployeeService sysEmployeeService;
    @Autowired
    private SysCompanyService sysCompanyService;
    @Autowired
    private MyKaptcha producer;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private LogOperateDataService logOperateDataService;
    @Autowired
    private SysOperatorService sysOperatorService;
    @Autowired
    private VPermissionAllotDataService vPermissionAllotDataService;
    @Autowired
    private WxMpService wxMpService;
    
    /**
     * ???????????????????????????????????????
     * 
     * @return ????????????
     */
    @PostMapping("/initLogin")
    public JsonModel initLogin(HttpSession session) {
        ManagerUser user = (ManagerUser) session.getAttribute(MayaConstant.LOGIN.MANAGER_LOGIN_USER_KEY);
        if (user != null) {
            return JsonModel.mkSuccess(user.getOptUserid()); // ????????????????????????
        }
        return JsonModel.mkFaile(MayaConstant.RETURN_STATUS_LOGIN, "???????????????"); // ???????????????????????????
    }
    
    
    /**
     * ????????????????????????
     * 
     * @return ????????????
     */
    @PostMapping("/check")
    public JsonModel check(HttpSession session, @RequestBody ManagerUser user) {
        String userId = user.getUserName();
        String userPwd = user.getUserPwd();
        String coName = user.getCoName();
        SysOperator bean = null;
        try {
        	if(!"????????????????????????????????????".equals(coName)) {
        		throw new BaseException("?????????????????????");
        	}
            if (StringUtils.isEmpty(userId)) {
                throw new BaseException("?????????????????????");
            }
            if (StringUtils.isEmpty(userPwd)) {
                throw new BaseException("???????????????!");
            }
            bean = this.sysLoginService.findManagerCheck(userId, userPwd, null);
            if (null == bean) {
                throw new BaseException("????????????????????????!");
            }
            if(bean.getCoName().equals(coName)) {
            	throw new BaseException("?????????"+coName+"???????????????????????????");
            }
            if(StringUtil.isNotBlank(user.getJpushId())) {
              //????????????????????????????????????
                sysOperatorService.editJpushIdByJpushId(user.getJpushId());
              //??????ID????????? ????????????????????????  ????????????????????????ID
                sysOperatorService.updateJpushId(bean.getId(),user.getJpushId());
            }
            //
            // ??????????????????
            SysCompany company = sysCompanyService.findById(bean.getCompanyId());
            if (null == company) {
                throw new BaseException("????????????????????????????????????????????????!");
            }
            if (company.getCoState() == null || company.getCoState() == 0) {
                throw new BaseException("????????????????????????????????????????????????");
            }
            //
            // ??????????????????
            SysEmployee employee = sysEmployeeService.findById(bean.getEmpId());
            if (null == employee) {
                throw new BaseException("????????????????????????????????????????????????!");
            }
            if (employee.getEmpState() == null || employee.getEmpState() == 0) {
                throw new BaseException("????????????????????????????????????????????????");
            }
            bean.setLegalEntityId(employee.getLegalEntityId());
            bean.setLegalEntityName(employee.getLegalEntityName());
            bean.setDeptName(employee.getDeptName());
            bean.setDeptId(employee.getDeptId());
            bean.setCoSupLegalCode(company.getCoSupLegalCode());
            bean.setCoLevel(company.getCoLevel());
            if (bean.getOptState() == null || bean.getOptState() == 0) {
                throw new BaseException("???????????????????????????????????????");
            }

            Date sysDate = sysLoginService.getSysDate();
            bean.setCoName(company.getCoName());
            bean.setCoAbbreviate(company.getCoAbbreviate());
            bean.setEmpName(employee.getEmpName());
            bean.setSysDate(DateUtil.getDateString(sysDate, DateUtil.G_DATE_FORMAT) + " " + DateUtil.getWeekOfDate(sysDate));

            // ?????????????????????redis
            ManagerUser managerUser = new ManagerUser();
            BeanUtils.copyProperties(bean, managerUser);
            this.login(managerUser);//??????token
            
            this.loadDataPermission(managerUser.getId());//??????????????????
            
            // ?????????????????????
            session.removeAttribute(MayaConstant.LOGIN.MANAGER_LOGIN_RANDOM);
            this.saveLoginLog(userId, "????????????");
            return JsonModel.dataResult(managerUser, "????????????");
        } catch (BaseException ex) {
            logger.error("????????????", ex);
            this.saveLoginLog(userId, ex.getMessage());
            return JsonModel.mkFaile(ex.getMessage());
        } catch (Exception ex) {
            this.saveLoginLog(userId, ex.getMessage());
            logger.error("????????????", ex);
        }
        return JsonModel.mkFaile("????????????");
    }
    
    
    @GetMapping("/loginAuto")
    public JsonModel  loginAuto (@RequestParam("code") String code) {
        logger.info("????????????????????????:code={}",code);
        WxOAuth2Service oAuth2Service =   wxMpService.getOAuth2Service();
        WxOAuth2AccessToken oAuth2AccessToken = null;
        String userId = null;
        try {
            oAuth2AccessToken =  oAuth2Service.getAccessToken(code);
        } catch (WxErrorException e) {
            logger.error("????????????????????????{}",e);
            e.printStackTrace();
            return JsonModel.mkFaile("????????????:{"+e.getMessage()+"}");
        }
        try {
            String openId = oAuth2AccessToken.getOpenId();
            //??????openId??????????????????
            SysOperator operator =  sysOperatorService.findByOpenId(openId);
            if(operator == null) {
                logger.error("???????????????????????????{}",openId);
                return JsonModel.mkFaile("???????????????????????????");
            }
            
            if(StringUtil.isNotBlank(operator.getJpushId())) {
                //????????????????????????????????????
                  sysOperatorService.editJpushIdByJpushId(operator.getJpushId());
                //??????ID????????? ????????????????????????  ????????????????????????ID
                  sysOperatorService.updateJpushId(operator.getId(),operator.getJpushId());
            }
            // ??????????????????
            SysCompany company = sysCompanyService.findById(operator.getCompanyId());
            if (null == company) {
                throw new BaseException("????????????????????????????????????????????????!");
            }
            if (company.getCoState() == null || company.getCoState() == 0) {
                throw new BaseException("????????????????????????????????????????????????");
            }

            // ??????????????????
            SysEmployee employee = sysEmployeeService.findById(operator.getEmpId());
            if (null == employee) {
                throw new BaseException("????????????????????????????????????????????????!");
            }
            if (employee.getEmpState() == null || employee.getEmpState() == 0) {
                throw new BaseException("????????????????????????????????????????????????");
            }
            operator.setLegalEntityId(employee.getLegalEntityId());
            operator.setLegalEntityName(employee.getLegalEntityName());
            operator.setDeptName(employee.getDeptName());
            operator.setDeptId(employee.getDeptId());
            operator.setCoSupLegalCode(company.getCoSupLegalCode());
            operator.setCoLevel(company.getCoLevel());
            if (operator.getOptState() == null || operator.getOptState() == 0) {
                throw new BaseException("???????????????????????????????????????");
            }

            Date sysDate = sysLoginService.getSysDate();
            operator.setCoName(company.getCoName());
            operator.setCoAbbreviate(company.getCoAbbreviate());
            operator.setEmpName(employee.getEmpName());
            operator.setSysDate(DateUtil.getDateString(sysDate, DateUtil.G_DATE_FORMAT) + " " + DateUtil.getWeekOfDate(sysDate));

            // ?????????????????????redis
            ManagerUser managerUser = new ManagerUser();
            BeanUtils.copyProperties(operator, managerUser);
            this.login(managerUser);//??????token
            
            this.loadDataPermission(managerUser.getId());//??????????????????
            logger.info("????????????????????????");
            return JsonModel.dataResult(managerUser); 
        } catch (BaseException ex) {
            logger.error("????????????", ex);
            this.saveLoginLog(userId, ex.getMessage());
            return JsonModel.mkFaile(ex.getMessage());
        } catch (Exception ex) {
            this.saveLoginLog(userId, ex.getMessage());
            logger.error("????????????", ex);
        }
        return JsonModel.mkFaile("????????????");

    }

    /**
     * ??????????????????????????????
     * 
     * @return ??????ctrl
     */
    @PostMapping("/logout")
    public JsonModel logout(HttpServletRequest request) {
        //??????session
        ManagerUser user =   super.getManagerUser();
        if (user != null) {
            if (sysLoginService.getSingleFlag()) {
                sysLoginService.downlineOpt(user.getOptUserid(), MayaConstant.SysType.MANAGER, null);
            }
            redisCacheService.deleteInMap(MayaConstant.CACHE.MANAGE_LOGIN_SESSION_ID, user.getOptUserid());
            //redisCacheService.deleteInMap(MayaConstant.CACHE.MANAGER_LOGIN_USER_KEY, user.getId());
        }
        //???????????? ??????jpushId
        sysOperatorService.updateJpushId(user.getId(),"");
        //??????????????????????????????????????????
        return JsonModel.mkFaile("????????????");
    }
    
    private void loadDataPermission(String optId) {
        /**??????????????????**/
        //????????????
        Map<String,Map<String,Set<String>>> map =vPermissionAllotDataService.findOptPermission(optId,BaseConstant.DataperCode.COMPANY_CODE);
        if(null!=map.get("readMap") &&map.get("readMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_COMPANY_READ, optId, map.get("readMap"));
        }
        if(null!=map.get("writeMap") &&map.get("writeMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_COMPANY_WRITE, optId, map.get("writeMap"));
        }
        //????????????
        map =vPermissionAllotDataService.findOptPermission(optId,BaseConstant.DataperCode.DEPT_CODE);
        if(null!=map.get("readMap") &&map.get("readMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_DEPT_READ, optId, map.get("readMap"));
        }
        if(null!=map.get("writeMap") &&map.get("writeMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_DEPT_WRITE, optId, map.get("writeMap"));
        }
        //????????????
        map =vPermissionAllotDataService.findOptPermission(optId,BaseConstant.DataperCode.WARE_CODE);
        if(null!=map.get("readMap") &&map.get("readMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_WARE_READ, optId, map.get("readMap"));
        }
        if(null!=map.get("writeMap") &&map.get("writeMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_WARE_WRITE, optId, map.get("writeMap"));
        }
        //?????????
        map =vPermissionAllotDataService.findOptPermission(optId,BaseConstant.DataperCode.EMP_CODE);
        if(null!=map.get("readMap") &&map.get("readMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_EMP_READ, optId, map.get("readMap"));
        }
        if(null!=map.get("writeMap") &&map.get("writeMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_EMP_WRITE, optId, map.get("writeMap"));
        }
        //????????????
        map =vPermissionAllotDataService.findOptPermission(optId,BaseConstant.DataperCode.ORG_CODE);
        if(null!=map.get("readMap") &&map.get("readMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_ORG_READ, optId, map.get("readMap"));
        }
        if(null!=map.get("writeMap") &&map.get("writeMap").size() > 0) {
            redisCacheService.addMapValue(MayaConstant.CACHE.PERMISSION_ORG_WRITE, optId, map.get("writeMap"));
        }
        /**??????????????????**/
    }


    /**
     * ???????????????????????????
     * 
     * @return ???????????????
     */

    @PostMapping("/captcha")
    public JsonModel captcha(HttpSession session) throws IOException {
        // ?????????????????????
        String text = producer.createText();
        System.out.println("????????????????????????" + text);
        session.setAttribute(MayaConstant.LOGIN.MANAGER_LOGIN_RANDOM, text);

        // ?????????????????????
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = producer.createImage(text);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        byte[] captchaChallengeAsJpeg = outputStream.toByteArray();

        return JsonModel.dataResult(captchaChallengeAsJpeg);
    }

    private void saveLoginLog(String userId, String resMsg) {
        SysOperator opt = sysOperatorService.findByOptUserid(userId);
        LogOperateData logData = new LogOperateData();
        logData.setId(UUID.randomUUID().toString());
        if (null != opt) {
            logData.setUserid(opt.getOptUserid());
            logData.setUserName(opt.getOptUsername());
        }
        logData.setLogTitle("??????????????????");
        logData.setRq(new Date());
        logData.setSiteType(Runtime.get(MayaConstant.SysType.RUN_TYPE));
        logData.setType(Runtime.get(MayaConstant.SysType.SITE_TYPE));
        logData.setRzType(LogType.TYPE2.getKey());
        logData.setResMsg(resMsg);
        logData.setLogLevel(LogLevel.LEVEL1.name());
        logData.setUip(HttpUtil.getIpAddr());

        this.logOperateDataService.add(logData);
    }
    
        //????????????????????????????????????????????????
        private TokenInfo login(ManagerUser user) {
            if (user == null) {
                throw new BaseException("???????????????????????????");
            }           
            String optId = user.getId();
            String accessToken = JwtUtils.createAccessToken(optId); 
            String refreshToken = JwtUtils.createRefreshToken(optId);
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            redisCacheService.addMapValue(MayaConstant.CACHE.MANAGER_LOGIN_USER_KEY, optId, user);
            return new TokenInfo(accessToken,refreshToken);
    }
}

class TokenInfo{
    
    private String accessToken;
    private String refreshToken;
    
    public TokenInfo(String accessToken, String refreshToken) {
        super();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    
}
