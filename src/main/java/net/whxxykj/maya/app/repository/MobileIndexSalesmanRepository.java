package net.whxxykj.maya.app.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import net.whxxykj.maya.common.repository.BaseRepository;
import net.whxxykj.maya.trade.entity.WarehouseSbill;

/**
 * 
 * @ClassName:  MobileIndexSalesmanRepository   
 * @Description: 首页业务员看面板   
 * @author: YEJUN
 * @date:   2021年6月23日 下午7:49:02      
 * @Copyright:
 */
@Repository
public interface MobileIndexSalesmanRepository extends BaseRepository<WarehouseSbill, String> {
  
    //根据日期查询当前的合同销售重量
    @Query(value = "select sum(a.goods_weight) from Sale_Scontract a where DATE_FORMAT(a.scontract_date,'%Y-%m-%d') = :dateStr  and if(:employeeCodes is null , 1=1 , employee_code in (:employeeCodes) ) ",nativeQuery = true)
    Double getSaleGoodsWeightByDate(@Param("dateStr") String dateStr,@Param("employeeCodes") Iterable<String> employeeCodes);
    
    //根据日期查询当前的合同采购重量 
    @Query(value = "select sum(a.goods_weight) from purchase_contract a where DATE_FORMAT(a.contract_date,'%Y-%m-%d') = :dateStr and if(:employeeCodes is null , 1=1 , employee_code in (:employeeCodes) )  ",nativeQuery = true)
    Double getPurchaseGoodsWeightByDate(@Param("dateStr") String dateStr,@Param("employeeCodes") Iterable<String> employeeCodes);
    
    //合同销售量前5
    @Query(value = "select sum(a.goods_weight) as goodsWeight ,max(partsname_name) as partsnameName ,max(goods_material) as goodsMaterial,max(pntree_name) as pntreeName,max(productarea_name) as productareaName,max(partsname_weightunit) as  partsnameWeightunit from v_sale_scontract_detail_report a where  if(:employeeCodes is null , 1=1 ,employee_code in (:employeeCodes) ) group by a.partsname_name,a.goods_material,a.pntree_name,a.productarea_name,a.partsname_weightunit order by sum(a.goods_weight) desc LIMIT 5",nativeQuery = true)
    List<Map<String,Object>> getSaleSumGoodsWeightTop5(@Param("employeeCodes") Iterable<String> employeeCodes);
    
    //合同销售额前5
    @Query(value = "select sum(a.goods_weight) as goodsBinmoney ,max(partsname_name) as partsnameName ,max(goods_material) as goodsMaterial,max(pntree_name) as pntreeName,max(productarea_name) as productareaName,max(partsname_weightunit) as  partsnameWeightunit from v_sale_scontract_detail_report a where  if(:employeeCodes is null , 1=1 ,employee_code in (:employeeCodes) ) group by a.partsname_name,a.goods_material,a.pntree_name,a.productarea_name,a.partsname_weightunit order by sum(a.goods_binmoney) desc LIMIT 5",nativeQuery = true)
    List<Map<String,Object>> getSaleSumGoodsMoneyTop5(@Param("employeeCodes") Iterable<String> employeeCodes);
    
     //合同销售量前5
    @Query(value = "select sum(a.goods_weight) as goodsWeight ,max(partsname_name) as partsnameName ,max(goods_material) as goodsMaterial,max(pntree_name) as pntreeName,max(productarea_name) as productareaName,max(partsname_weightunit) as  partsnameWeightunit from v_purchase_contract_detail_report a  where  if(:employeeCodes is null , 1=1 ,employee_code in (:employeeCodes) ) group by a.partsname_name,a.goods_material,a.pntree_name,a.productarea_name,a.partsname_weightunit order by sum(a.goods_weight) desc LIMIT 5",nativeQuery = true)
    List<Map<String,Object>> getPurchaseSumGoodsWeightTop5(@Param("employeeCodes") Iterable<String> employeeCodes);
    
    //合同销售额前5
    @Query(value = "select sum(a.goods_inmoney) as goodsMoney ,max(partsname_name) as partsnameName ,max(goods_material) as goodsMaterial,max(pntree_name) as pntreeName,max(productarea_name) as productareaName,max(partsname_weightunit) as  partsnameWeightunit from v_purchase_contract_detail_report a  where  if(:employeeCodes is null , 1=1 ,employee_code in (:employeeCodes) ) group by a.partsname_name,a.goods_material,a.pntree_name,a.productarea_name,a.partsname_weightunit order by sum(a.goods_inmoney) desc LIMIT 5",nativeQuery = true)
    List<Map<String,Object>> getPurchaseSumGoodsMoneyTop5(@Param("employeeCodes") Iterable<String> employeeCodes);
    
    //客户销售金额top5
    @Query(value = "select sum(a.goods_inmoney) as goodsMoney, a.datas_balcorpname as datasBalcorpname from v_purchase_contract_detail_report a where  if(:employeeCodes is null , 1=1 ,employee_code in (:employeeCodes) ) GROUP BY a.datas_balcorpname order by sum(a.goods_inmoney) desc LIMIT 5",nativeQuery = true)
    List<Map<String,Object>> getCustomerGoodsMoneyTop5(@Param("employeeCodes") Iterable<String> employeeCodes);
    
    //客户销售重量top5
    @Query(value = "select sum(a.goods_weight) as goodsWeight, a.datas_balcorpname as datasBalcorpname,a.partsname_weightunit as partsnameWeightunit from v_purchase_contract_detail_report a where  if(:employeeCodes is null , 1=1 ,employee_code in (:employeeCodes) ) GROUP BY a.datas_balcorpname,a.partsname_weightunit order by sum(a.goods_weight) desc LIMIT 5",nativeQuery = true)
    List<Map<String,Object>> getCustomerGoodsWeightTop5(@Param("employeeCodes") Iterable<String> employeeCodes);
    
    //供应商采购金额top5
    @Query(value = "select sum(a.goods_binmoney) as goodsMoney, a.datas_balcorpname as datasBalcorpname from v_sale_scontract_detail_report a where  if(:employeeCodes is null , 1=1 ,employee_code in (:employeeCodes) ) GROUP BY a.datas_balcorpname order by sum(a.goods_binmoney) desc LIMIT 5",nativeQuery = true)
    List<Map<String,Object>> getSupplierGoodsMoneyTop5(@Param("employeeCodes") Iterable<String> employeeCodes);
    
     //供应商采购重量top5
    @Query(value = "select sum(a.goods_weight) as goodsWeight, a.datas_balcorpname as datasBalcorpname,a.partsname_weightunit as partsnameWeightunit from v_sale_scontract_detail_report a where  if(:employeeCodes is null , 1=1 ,employee_code in (:employeeCodes) ) GROUP BY a.datas_balcorpname,a.partsname_weightunit order by sum(a.goods_weight) desc LIMIT 5",nativeQuery = true)
    List<Map<String,Object>> getSupplierGoodsWeightTop5(@Param("employeeCodes") Iterable<String> employeeCodes);
}
