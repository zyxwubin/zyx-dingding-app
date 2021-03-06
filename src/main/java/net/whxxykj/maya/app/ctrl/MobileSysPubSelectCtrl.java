package net.whxxykj.maya.app.ctrl;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.whxxykj.maya.base.entity.SysBillSetting;
import net.whxxykj.maya.base.entity.SysBpmnSetting;
import net.whxxykj.maya.base.entity.SysButton;
import net.whxxykj.maya.base.entity.SysCode;
import net.whxxykj.maya.base.entity.SysCompany;
import net.whxxykj.maya.base.entity.SysCurrency;
import net.whxxykj.maya.base.entity.SysDept;
import net.whxxykj.maya.base.entity.SysEmployee;
import net.whxxykj.maya.base.entity.SysFeeitem;
import net.whxxykj.maya.base.entity.SysInneraccounts;
import net.whxxykj.maya.base.entity.SysMenu;
import net.whxxykj.maya.base.entity.SysMenuDir;
import net.whxxykj.maya.base.entity.SysOrg;
import net.whxxykj.maya.base.entity.SysOrgAccounts;
import net.whxxykj.maya.base.entity.SysOrgBank;
import net.whxxykj.maya.base.entity.SysOrgLinkman;
import net.whxxykj.maya.base.entity.SysPermAllot;
import net.whxxykj.maya.base.entity.SysPntree;
import net.whxxykj.maya.base.entity.SysProject;
import net.whxxykj.maya.base.entity.SysSubSite;
import net.whxxykj.maya.base.entity.SysTermcode;
import net.whxxykj.maya.base.entity.SysWarehouse;
import net.whxxykj.maya.base.entity.SysWareplace;
import net.whxxykj.maya.base.service.SysBillSettingService;
import net.whxxykj.maya.base.service.SysBpmnSettingService;
import net.whxxykj.maya.base.service.SysButtonService;
import net.whxxykj.maya.base.service.SysCodeService;
import net.whxxykj.maya.base.service.SysCompanyService;
import net.whxxykj.maya.base.service.SysCurrencyService;
import net.whxxykj.maya.base.service.SysDeptService;
import net.whxxykj.maya.base.service.SysEmployeeService;
import net.whxxykj.maya.base.service.SysFeeitemService;
import net.whxxykj.maya.base.service.SysGoodscodeService;
import net.whxxykj.maya.base.service.SysInneraccountsService;
import net.whxxykj.maya.base.service.SysMenuService;
import net.whxxykj.maya.base.service.SysOrgAccountsService;
import net.whxxykj.maya.base.service.SysOrgBankService;
import net.whxxykj.maya.base.service.SysOrgLinkmanService;
import net.whxxykj.maya.base.service.SysOrgService;
import net.whxxykj.maya.base.service.SysPartsnameService;
import net.whxxykj.maya.base.service.SysPntreeService;
import net.whxxykj.maya.base.service.SysProCoService;
import net.whxxykj.maya.base.service.SysProjectService;
import net.whxxykj.maya.base.service.SysSubSiteService;
import net.whxxykj.maya.base.service.SysTermcodeService;
import net.whxxykj.maya.base.service.SysWarehouseService;
import net.whxxykj.maya.base.service.SysWareplaceService;
import net.whxxykj.maya.common.ctrl.BaseCtrl;
import net.whxxykj.maya.common.entity.ManagerUser;
import net.whxxykj.maya.common.exception.BaseException;
import net.whxxykj.maya.common.exception.ExcelException;
import net.whxxykj.maya.common.repository.QueryBean;
import net.whxxykj.maya.common.util.JsonModel;

@RestController
@RequestMapping("/mobile/sysm/pubselect")
public class MobileSysPubSelectCtrl extends BaseCtrl<SysCodeService, SysCode> {

    @Autowired
    private SysCodeService sysCodeService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysButtonService sysButtonService;

    @Autowired
    private SysCompanyService sysCompanyService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysSubSiteService sysSubSiteService;

    @Autowired
    private SysProjectService sysProjectService;

    @Autowired
    private SysProCoService sysProCoService;

    @Autowired
    private SysEmployeeService sysEmployeeService;

    @Autowired
    private SysOrgService sysOrgService;

    @Autowired
    private SysOrgAccountsService sysOrgAccountsService;

    @Autowired
    private SysOrgBankService sysOrgBankService;

    @Autowired
    private SysOrgLinkmanService sysOrgLinkmanService;

    @Autowired
    private SysWarehouseService sysWarehouseService;

    @Autowired
    private SysGoodscodeService sysGoodscodeService;

    @Autowired
    private SysInneraccountsService sysInneraccountsService;

    @Autowired
    private SysFeeitemService sysFeeitemService;

    @Autowired
    private SysPartsnameService sysPartsnameService;

    private SysBillSettingService sysBillSettingService;

    @Autowired
    private SysPntreeService sysPntreeService;

    @Autowired
    private SysWareplaceService sysWareplaceService;

    @Autowired
    private SysTermcodeService sysTermcodeService;

    @Autowired
    private SysBpmnSettingService sysBpmnSettingService;
    @Autowired
    private SysCurrencyService sysCurrencyService;
    /**
     * ???????????????????????????Id????????????
     * 
     * @param subsiteId
     *            ?????????ID
     * @return
     */
    @GetMapping("/loadMenu")
    public JsonModel loadMenu(@RequestParam String subsiteId) {
        List<SysMenuDir> list = sysMenuService.findMenuDir(subsiteId);
        return JsonModel.dataResult(list.size(), list);
    }

    @GetMapping("/loadMenu2")
    public JsonModel loadMenu2(@RequestParam String siteId) {
        List<SysSubSite> list = sysSubSiteService.findBySiteId(siteId);
        for (SysSubSite sysSubSite : list) {
            List<SysMenuDir> menuDirList = sysMenuService.findMenuDir(sysSubSite.getId());
            Collections.sort(menuDirList, new Comparator<SysMenuDir>() {
                @Override
                public int compare(SysMenuDir o1, SysMenuDir o2) {
                    return o2.getMenuDirState() - o1.getMenuDirState();
                }

            });
            sysSubSite.setSysMenuDirList(menuDirList);
        }
        return JsonModel.dataResult(list.size(), list);
    }

    /**
     * t????????????Id ????????????????????????
     * 
     * @param menuDirId
     * @return
     */
    @GetMapping("/findMenuByMenuDirId")
    public JsonModel findMenuByMenuDirId(@RequestParam String menuDirId) {
        List<SysMenu> sysMenus = sysMenuService.findMenuByMenuDirId(menuDirId);
        return JsonModel.dataResult(sysMenus);
    }

    // ????????????????????????????????????
    @GetMapping("/getCodeByTypes")
    public JsonModel getCodeByTypes(String types) {
        Map<String, List<SysCode>> map = sysCodeService.findByTypesState(types, null);
        return JsonModel.dataResult(map);
    }

    /**
     * ????????????????????????ID?????????ID????????????
     * 
     * @param subsiteId
     *            ?????????ID
     * @param menuId
     *            ??????ID
     * @return
     */
    @GetMapping("/findButtonBySiteAndMenu")
    public JsonModel findButtonBySiteAndMenu(@RequestParam String subsiteId, @RequestParam String menuId) {
        List<SysButton> list = sysButtonService.findBySubsiteAndMenu(subsiteId, menuId);
        return JsonModel.dataResult(list.size(), list);
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     * 
     * @param subsiteId
     *            ?????????ID
     * @param menuId
     *            ??????ID
     * @return
     */
    @GetMapping("/findButtonQx")
    public JsonModel findButtonQx(@RequestParam String subsiteId, @RequestParam String menuId) {
        ManagerUser user = this.getManagerUser();
        if (user == null) {
            throw new BaseException("???????????????");
        }
        Integer optIdentity = user.getOptIdentity();
        String companyId = user.getCompanyId();
        
        List<SysButton> list = null;
        if (Integer.valueOf(2).compareTo(optIdentity) == 0) {
            list = sysButtonService.findAllBySubsiteIdAndMenuId(subsiteId, menuId);
        } else {
            list = sysButtonService.findButtonQx(user.getId(), SysPermAllot.EntType.TYPE1, subsiteId, menuId);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            //????????????????????????
            SysBpmnSetting  sysBpmnSetting = sysBpmnSettingService.findBpmnByCompanyIdAndMenuId(companyId,menuId);
            if (sysBpmnSetting == null) {
                Iterator<SysButton> it = list.iterator();
                while (it.hasNext()) {
                    SysButton button = it.next();
                    Integer btnType = button.getBtnType();
                    if (btnType != null) {
                        if((btnType == 9 || btnType == 11 || btnType == 12) && sysBpmnSetting == null) {
                            it.remove();
                        }
                    }
                }
            }
        }
        return JsonModel.dataResult(list.size(), list);
    }

    /**
     * ????????????????????????????????????
     * 
     * @return
     */

    @GetMapping("/getSysDateTime")
    public JsonModel getSysDateTime() {
        return JsonModel.dataResult(sysCodeService.getSysDate());
    }

    // ???????????????????????????-????????????
    /**
     * @Title: findChildCompanyListByOpt
     * @Description: ?????????????????????????????????????????????????????????????????????<br>
     * @Parmaters: @param queryBean
     * @Parmaters: @return
     * @Return: JsonModel
     * @Throws:
     * @Author:lijun
     * @CreateDate:2020???6???27??? ??????11:59:14
     * @ModifyLog:2020???6???27??? ??????11:59:14
     */
    @PostMapping("/findChildCompanyListByOpt")
    public JsonModel findChildCompanyListByOpt(@RequestBody QueryBean queryBean) {
        // ?????????????????????????????????????????????????????????????????????????????????????????????
        queryBean.setDirStr("asc");
        queryBean.setSortStr("coCode");
        ManagerUser user = this.getManagerUser();
        Integer optIdentity = user.getOptIdentity();
        Page<SysCompany> page = null;
        if (Integer.valueOf(2).compareTo(optIdentity) != 0) {
            // ??????????????????????????????????????????????????????????????????
            List<String> permCompanyList = user.getDataPermCompanyList();
            queryBean.defalutParam("id_in", permCompanyList);
        }
        page = sysCompanyService.findListAll(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * ???????????? ??????-???????????? ????????????????????????
     * 
     * @param queryBean
     * @return
     * @throws ExcelException
     * @throws IOException
     */
    @PostMapping(value = "/findProjectListAll")
    @ResponseBody
    public JsonModel findProjectListAll(@RequestBody QueryBean queryBean) {
        queryBean.setSortStr("cDate");
        queryBean.setDirStr("desc");
        ManagerUser user = this.getManagerUser();
        Integer optIdentity = user.getOptIdentity();
        if (Integer.valueOf(2).compareTo(optIdentity) != 0) {
            // ???????????????????????????????????????????????????
            List<String> permCompanyList = user.getDataPermCompanyList();
            Set<String> proIds = sysProCoService.findByCoIds(permCompanyList).stream().map(x -> x.getProId())
                .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(proIds)) {
                queryBean.defalutParam("id_in", proIds);
            } else {
                queryBean.defalutParam("id_in", "");
            }
        }
        Page<SysProject> page = sysProjectService.findProjectListAll(queryBean);
        return new JsonModel(page.getTotalElements(), page.getContent());
    }

    /**
     * @Title: findListAll
     * @Description: ????????????????????????????????????????????????<br>
     * @Parmaters: @param queryBean
     * @Return: JsonModel
     * @Throws:
     * @Author:lijun
     * @CreateDate:2020???6???27??? ??????4:12:39
     * @ModifyLog:2020???6???27??? ??????4:12:39
     */
    @PostMapping("/findChildCompanyListAll")
    public JsonModel findChildCompanyListAll(@RequestBody QueryBean queryBean) {
        // ?????????????????????????????????????????????????????????????????????????????????????????????
        queryBean.setDirStr("asc");
        queryBean.setSortStr("coCode");
        ManagerUser user = this.getManagerUser();
        Integer optIdentity = user.getOptIdentity();
        Page<SysCompany> page = null;
        if (Integer.valueOf(2).compareTo(optIdentity) != 0) {
            // ??????????????????????????????????????????????????????????????????
            page = sysCompanyService.findChildCompanyList(queryBean);
        } else {
            page = sysCompanyService.findListAll(queryBean);
        }
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * ????????????????????????????????????????????? ??????????????????
     * 
     * @param queryBean
     * @return
     */
    @PostMapping("/findChildCompanyList")
    public JsonModel findChildCompanyList(@RequestBody QueryBean queryBean) {
        ManagerUser user = this.getManagerUser();
        queryBean.defalutParam("coSupLegalId_eq", user.getCompanyId());
        Page<SysCompany> page = sysCompanyService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findCompanyList @Description: ?????????????????? ??????????????? @param: @param queryBean @param: @return @return:
     * JsonModel @throws
     */
    @PostMapping("/findCompanyList")
    public JsonModel findCompanyList(@RequestBody QueryBean queryBean) {
        // ????????????
        ManagerUser user = this.getManagerUser();
//        DataPermissionUtil.getInstance().getCompanyReadPermisssion(queryBean, user, "id");
        Page<SysCompany> page = sysCompanyService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findCompanyListNoPermission 
     * @Description: ?????????????????? ????????????????????? 
     * @param: queryBean 
     * @return: JsonModel 
     * @throws
     */
    @PostMapping("/findCompanyListNoPermission")
    public JsonModel findCompanyListNoPermission(@RequestBody QueryBean queryBean) {
        Page<SysCompany> page = sysCompanyService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findDeptListNoPermission   
     * @Description: ???????????? ???????????????   
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findDeptListNoPermission")
    public JsonModel findDeptListNoPermission(@RequestBody QueryBean queryBean) {
        Page<SysDept> page = sysDeptService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findDeptList   
     * @Description: ?????????????????? ???????????????   
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findDeptList")
    public JsonModel findDeptList(@RequestBody QueryBean queryBean) {
        // ????????????
        ManagerUser user = this.getManagerUser();
//        DataPermissionUtil.getInstance().getDeptReadPermisssion(queryBean, user, "id");
        Page<SysDept> page = sysDeptService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * ??????????????????
     * 
     * @param queryBean
     *            ??????????????????
     * @return
     */
    @PostMapping("/findEmployeeList")
    public JsonModel findEmployeeList(@RequestBody QueryBean queryBean) {
        queryBean.setSortStr("id");
        queryBean.setDirStr("desc");
        queryBean.defalutParam("empWorking_eq", "1");
        // queryBean.defalutParam("empState_eq", 1);
        // ????????????
        ManagerUser user = this.getManagerUser();
//        DataPermissionUtil.getInstance().getCompanyReadPermisssion(queryBean, user, "companyId");
//        DataPermissionUtil.getInstance().getDeptReadPermisssion(queryBean, user, "deptId");
        Page<SysEmployee> page = sysEmployeeService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * ????????????????????????
     * 
     * @param queryBean
     *            ??????????????????
     * @return
     */
    @PostMapping("/findInnerCompanyList")
    public JsonModel findInnerCompanyList(@RequestBody QueryBean queryBean) {
        ManagerUser user = this.getManagerUser();
        queryBean.defalutParam("coSupLegalCode_startlike", user.getCoSupLegalCode());
        Page<SysCompany> page = sysCompanyService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * ??????????????????
     * 
     * @param queryBean
     *            ??????????????????
     * @return
     */
    @PostMapping("/findOrgList")
    public JsonModel findOrgList(@RequestBody QueryBean queryBean) {
//        DataPermissionUtil.getInstance().getOrgCodeReadPermission(queryBean, this.getManagerUser(), "id");
        Page<SysOrg> page = sysOrgService.findCombo(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findOrgAccountsList   
     * @Description: ????????????????????????   
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findOrgAccountsList")
    public JsonModel findOrgAccountsList(@RequestBody QueryBean queryBean) {
        Page<SysOrgAccounts> page = sysOrgAccountsService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findOrgBankList @Description: ?????????????????? @param: @param queryBean @param: @return @return: JsonModel @throws
     */
    @PostMapping("/findOrgBankList")
    public JsonModel findOrgBankList(@RequestBody QueryBean queryBean) {
        Page<SysOrgBank> page = sysOrgBankService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findOrgBankList 
     * @Description: ????????????????????? 
     * @param: @param queryBean 
     * @param: @return 
     * @return: JsonModel 
     * @throws
     */
    @PostMapping("/findOrgLinkmanList")
    public JsonModel findsysOrgLinkmanList(@RequestBody QueryBean queryBean) {
        Page<SysOrgLinkman> page = sysOrgLinkmanService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findTermsettingList 
     * @Description: ???????????????????????? 
     * @param: @param queryBean 
     * @param: @return 
     * @return:JsonModel 
     * @throws
     */
    @GetMapping("/findTermsettingList")
    public JsonModel findTermsettingList(@RequestParam String billtypeCode) {
        List<SysTermcode> list = sysTermcodeService.findByBilltypeCode(billtypeCode);
        return JsonModel.dataResult(list);
    }

    /**
     * 
     * @Title: findTermsettingList @Description: ???????????????????????? @param: @param queryBean @param: @return @return:
     * JsonModel @throws
     */
    @GetMapping("/querySysTermsettingList")
    public JsonModel querySysTermsettingList(@RequestParam String billtypeCode, String termsettingCode) {
        List<SysTermcode> termcodeList = sysTermcodeService.findByBilltypeCode(billtypeCode);
        return JsonModel.dataResult(termcodeList);
    }

    /**
     * 
     * @Title: findTermsettingList @Description: ?????????????????? @param: @param queryBean @param: @return @return:
     * JsonModel @throws
     */
    @PostMapping("/findWarehouseList")
    public JsonModel findWarehouseList(@RequestBody QueryBean queryBean) {
        // ????????????
        ManagerUser user = this.getManagerUser();
//        DataPermissionUtil.getInstance().getWareReadPermisssion(queryBean, user);
        Page<SysWarehouse> page = sysWarehouseService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findWarehouseListNoPermission @Description: ???????????????????????????????????? @param: @param
     * queryBean @param: @return @return: JsonModel @throws
     */
    @PostMapping("/findWarehouseListNoPermission")
    public JsonModel findWarehouseListNoPermission(@RequestBody QueryBean queryBean) {
        Page<SysWarehouse> page = sysWarehouseService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findGoodsMaterialList @Description: ?????????????????? @param: @param queryBean @param: @return @return:
     * JsonModel @throws
     */
    @PostMapping("/findGoodsMaterialList")
    public JsonModel findGoodsMaterialList(@RequestBody QueryBean queryBean) {
        Page<Map<String, String>> page = sysGoodscodeService.queryGoodsMaterialList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findInneraccountsList @Description: ???????????????????????? @param: @param queryBean @param: @return @return:
     * JsonModel @throws
     */
    @PostMapping("/findInneraccountsList")
    public JsonModel findInneraccountsList(@RequestBody QueryBean queryBean) {
        Page<SysInneraccounts> page = sysInneraccountsService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findFeeitemList @Description: ?????????????????? @param: @param queryBean @param: @return @return: JsonModel @throws
     */
    @PostMapping("/findFeeitemList")
    public JsonModel findFeeitemList(@RequestBody QueryBean queryBean) {
        Page<SysFeeitem> page = sysFeeitemService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findPartsnameList   
     * @Description: ??????????????????   
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
   @PostMapping("/findPartsnameList")
   public JsonModel findPartsnameList (@RequestBody QueryBean queryBean){
       Page<Map<String, String>> page = sysPartsnameService.queryPartsnameList(queryBean);
       return JsonModel.dataResult(page.getTotalElements(), page.getContent());
   }

    /**
     * 
     * @Title: findGoodsMaterialList @Description: ?????????????????? @param: @param queryBean @param: @return @return:
     * JsonModel @throws
     */
    @PostMapping("/findProductareaList")
    public JsonModel findProductareaList(@RequestBody QueryBean queryBean) {
        Page<Map<String, String>> page = sysGoodscodeService.findProductareaList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: findGoodsMaterialList @Description: ???????????? ?????????????????? @param: @param queryBean @param: @return @return:
     * JsonModel @throws
     */
    @PostMapping("/findSysPntreeList")
    public JsonModel findSysPntreeList(@RequestBody QueryBean queryBean) {
        // Page<SysPntree> page = sysPntreeService.queryPageList(queryBean);
        List<SysPntree> list = sysPntreeService.findByPntreeParentcode();
        return JsonModel.dataResult(list.size(), list);
    }
    /**
     * 
     * @Title: findGoodsMaterialList   
     * @Description: ????????????  ?????? ??????  ????????????
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findSysPntreeList1")
    public JsonModel findSysPntreeList1 (@RequestBody QueryBean queryBean){
        Page<Map<String, String>> page  = sysGoodscodeService.queryPntreeNameList(queryBean);
//      List<SysPntree> list = sysPntreeService.findByPntreeParentcode();
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    @PostMapping("/findSysWareplaceList")
    public JsonModel findSysWareplaceList(@RequestBody QueryBean queryBean) {
        queryBean.defalutParam("warehouseWareplace_eq", "1");// ?????????????????????
        Page<SysWareplace> page = sysWareplaceService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }

    /**
     * 
     * @Title: getBillTypeCode @Description: ??????????????????????????????0???????????????1??????????????? @param: @param type @param: @return @return:
     * JsonModel @throws
     */
    @GetMapping("/getBillCodeMode/{type}")
    public JsonModel getBillTypeCode(@PathVariable(value = "type") String type) {
        SysBillSetting setting = sysBillSettingService.findByBillTypeCode(type);
        return JsonModel.dataResult(setting.getBillCodeMode());
    }
    
    @PostMapping("/findSysCurrencyList")
    public JsonModel findSysCurrencyList(@RequestBody QueryBean queryBean) {
        Page<SysCurrency> page = sysCurrencyService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent());
    }
}
