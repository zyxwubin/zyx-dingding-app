package net.whxxykj.maya.app.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.whxxykj.maya.base.BaseConstant;
import net.whxxykj.maya.base.common.uitls.DataPermissionNewUtil;
import net.whxxykj.maya.common.ctrl.BaseCtrl;
import net.whxxykj.maya.common.repository.QueryBean;
import net.whxxykj.maya.common.util.JsonModel;
import net.whxxykj.maya.trade.entity.InvoiceSaleinv;
import net.whxxykj.maya.trade.service.InvoiceSaleinvService;

@RestController
@RequestMapping(value = "/mobile/tradem/invoicesaleinv")
public class MobileInvoiceSaleinvCtrl extends BaseCtrl<InvoiceSaleinvService, InvoiceSaleinv> {
    
    @Autowired
    private InvoiceSaleinvService invoiceSaleinvService;
    
    @GetMapping(value = "/updateInvalid")
    public JsonModel updateInvalid(@RequestParam String saleinvBillcode){
        InvoiceSaleinv mod = invoiceSaleinvService.updateInvalid(saleinvBillcode);
        return JsonModel.dataResult(mod, "作废成功");
    }

    @GetMapping("/updateSubmit")
    public JsonModel updateSubmit(@RequestParam String id, @RequestParam String submit) {
        invoiceSaleinvService.updateSubmit(id, submit);
        return JsonModel.dataResult("提交成功");
    }
    
    @Override
    public JsonModel findList(@RequestBody QueryBean queryBean) {
        DataPermissionNewUtil.getInstance().getReadPermission(this.getManagerUser(), BaseConstant.PermType.PERMTYPE_FPXG, queryBean);
//        DataPermissionUtil.getInstance().getReadPermisssion(queryBean, this.getManagerUser());
        return super.findList(queryBean);
    }
}
