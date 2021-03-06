package net.whxxykj.maya.app.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.whxxykj.maya.base.BaseConstant;
import net.whxxykj.maya.base.common.uitls.DataPermissionNewUtil;
import net.whxxykj.maya.base.service.SysBillSettingService;
import net.whxxykj.maya.common.ctrl.BaseCtrl;
import net.whxxykj.maya.common.repository.QueryBean;
import net.whxxykj.maya.common.util.JsonModel;
import net.whxxykj.maya.common.util.StringUtil;
import net.whxxykj.maya.trade.entity.VWarehouseSbillDetailReport;
import net.whxxykj.maya.trade.entity.WarehouseSbill;
import net.whxxykj.maya.trade.service.VWarehouseSbillDetailReportService;
import net.whxxykj.maya.trade.service.WarehouseSbillDetailService;
import net.whxxykj.maya.trade.service.WarehouseSbillService;

@RestController
@RequestMapping(value = "/mobile/tradem/warehouseSbill")
public class MobileWarehouseSbillCtrl extends BaseCtrl<WarehouseSbillService, WarehouseSbill> {

    @Autowired
    private WarehouseSbillService warehouseSbillService;
    
    @Autowired
    private WarehouseSbillDetailService warehouseSbillDetailService;
    
    @Autowired
    private VWarehouseSbillDetailReportService vWarehouseSbillDetailReportService;
    
    @Autowired
    private SysBillSettingService billSettingService;
    
    @Override
    public JsonModel add(@RequestBody WarehouseSbill mod) {
        mod.setBilltypeCode(BaseConstant.BillTypeCode.XHTD);
        return super.add(mod);
    }
    
    @Override
    public JsonModel findList(@RequestBody QueryBean queryBean) {
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_XSXG, queryBean);
        Map<String, Object>  searchFileds =  queryBean.getSearchFileds();
        searchFileds.put("billtypeCode_eq", BaseConstant.BillTypeCode.XHTD);
        return super.findList(queryBean);
    }
    

    @GetMapping("/updateSubmit")
    public JsonModel updateSubmit(@RequestParam String id, @RequestParam String submit) {
        warehouseSbillService.updateSubmit(id, submit);
        return JsonModel.dataResult("????????????");
    }
    
    /**
     * @Title: findAllList   
     * @Description:??????????????????
     * @param: @param queryBean
     * @param: @return
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findAllList/list")
    @ResponseBody
    public JsonModel findAllList(@RequestBody QueryBean queryBean) {
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_XSXG, queryBean);
        return super.findList(queryBean);
    }
    
    /**
     * 
     * @Title: findDetailReportList   
     * @Description: ????????????????????????   
     * @param: @return
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findDetailReport/list")
    public JsonModel findDetailReportList(@RequestBody QueryBean queryBean) {
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_XSXG, queryBean);
        Page<VWarehouseSbillDetailReport> page = vWarehouseSbillDetailReportService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent(),null,queryBean.getAllSum());
    }
    /**
     * 
     * @Title: findDetailReportList   
     * @Description: ????????????????????????   
     * @param: @return
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findSumReport/list")
    public JsonModel findSumReportList(@RequestBody QueryBean queryBean) {
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_XSXG, queryBean);
        Page<VWarehouseSbillDetailReport> page = vWarehouseSbillDetailReportService.querySumPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent(),null,queryBean.getAllSum());
    }
    
    
    /**
     * 
     * @Title: findDetailStockQuoteList   
     * @Description: ???????????????(??????)  
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findDetailStockQuoteList")
    public JsonModel findDetailStockQuoteList(@RequestBody QueryBean queryBean) {
        //????????????
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_XSXG, queryBean);
        String billtypeCode = BaseConstant.BillTypeCode.XHTD;
        if(billSettingService.getBillVerify(billtypeCode)) { //????????????
            Map<String, Object> searchFileds = queryBean.getSearchFileds();
            searchFileds.put("dataAudit_eq", BaseConstant.AuditStatus.YS); //????????????????????????
        }
        queryBean.defalutParam("billtypeCode_eq", billtypeCode); //??????????????????
        queryBean.defalutParam("dataGoodsflag_ne", BaseConstant.DataGoodsflag.flag_1); //??????????????????
        Page<VWarehouseSbillDetailReport> page = vWarehouseSbillDetailReportService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent(),null,queryBean.getAllSum());
    }
    
    /**
     * 
     * @Title: findDetailContractQuoteList   
     * @Description: ???????????????(??????)  
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findDetailContractQuoteList")
    public JsonModel findDetailQuoteList(@RequestBody QueryBean queryBean) {
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_XSXG, queryBean);
        String billtypeCode = BaseConstant.BillTypeCode.PHTD;
        if(billSettingService.getBillVerify(billtypeCode)) { //????????????
            Map<String, Object> searchFileds = queryBean.getSearchFileds();
            searchFileds.put("dataAudit_eq", BaseConstant.AuditStatus.YS); //????????????????????????
        }
        queryBean.defalutParam("billtypeCode_eq", billtypeCode); //??????????????????
        queryBean.defalutParam("dataGoodsflag_ne", BaseConstant.DataGoodsflag.flag_1); //??????????????????
        Page<VWarehouseSbillDetailReport> page = vWarehouseSbillDetailReportService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent(),null,queryBean.getAllSum());
    }
    
    /**
     * 
     * @Title: findDetailReturnQuoteList   
     * @Description: ???????????????????????????   
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findDetailReturnQuoteList")
    public JsonModel findDetailReturnQuoteList(@RequestBody QueryBean queryBean) {
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_XSXG, queryBean);
        String billtypeCode = BaseConstant.BillTypeCode.TDTH;
        if(billSettingService.getBillVerify(billtypeCode)) { //????????????
            Map<String, Object> searchFileds = queryBean.getSearchFileds();
            searchFileds.put("dataAudit_eq", BaseConstant.AuditStatus.YS); //????????????????????????
        }
        queryBean.defalutParam("billtypeCode_eq", billtypeCode); //??????????????????
        queryBean.defalutParam("dataGoodsflag_ne", BaseConstant.DataGoodsflag.flag_1);
        //searchFileds.put("sbillDetailOkweight_gt", 0); //??????????????????0
        Page<VWarehouseSbillDetailReport> page = vWarehouseSbillDetailReportService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent(),null,queryBean.getAllSum());
    }
    
    /**
     * 
     * @Title: findDetailQuoteList   
     * @Description: ???????????????   
     * @param: @param queryBean
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @PostMapping("/findDetailReturnList")
    public JsonModel findDetailReturnList(@RequestBody QueryBean queryBean) {
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_XSXG, queryBean);
        Map<String, Object> searchFileds = queryBean.getSearchFileds();
        String billtypeCode =  (String)searchFileds.get("billtypeCode_eq");
        if(StringUtil.isEmpty(billtypeCode)) { //??????????????????
            searchFileds.put("billtypeCode_ne", BaseConstant.BillTypeCode.TDTH); //???????????????????????? ?????? ??????(??????\??????)
        }
        searchFileds.put("sbillDetailOkweight_gt", 0); //??????????????????0
        Page<VWarehouseSbillDetailReport> page = vWarehouseSbillDetailReportService.queryPageList(queryBean);
        return JsonModel.dataResult(page.getTotalElements(), page.getContent(),null,queryBean.getAllSum());
    }
    /**
     * 
     * @Title: updateGoodsPrice   
     * @Description: ?????????  ???????????? 
     * @param: @param batchs ????????????????????????
     * @param: @param type ????????????   0:???????????? 1:????????? 2:?????????
     * @param: @param price 
     * @param: @return      
     * @return: JsonModel      
     * @throws
     */
    @GetMapping("/updateGoodsBinprice")
    public JsonModel updateGoodsBinprice(@RequestParam String batchs,@RequestParam String type,@RequestParam Double price) {
        int i = warehouseSbillDetailService.updateGoodsBinprice(batchs,type,price);
        return JsonModel.mkSuccess("????????????");
    }
}
