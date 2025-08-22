package com.example.managementapi.Service;


import com.example.managementapi.Dto.Request.Supplier.CreateSupplierReq;
import com.example.managementapi.Dto.Request.Supplier.UpdateSupplierReq;
import com.example.managementapi.Dto.Response.Supplier.CreateSupplierRes;
import com.example.managementapi.Dto.Response.Supplier.GetSupplierDetailRes;
import com.example.managementapi.Dto.Response.Supplier.GetSupplierRes;
import com.example.managementapi.Dto.Response.Supplier.UpdateSupplierRes;
import com.example.managementapi.Entity.Supplier;
import com.example.managementapi.Mapper.SupplierMapper;
import com.example.managementapi.Repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierService {

    @Autowired
    private final SupplierRepository supplierRepository;

    @Autowired
    private final SupplierMapper supplierMapper;

    public List<GetSupplierRes> getSuppliers(){
        return supplierRepository.findAll().stream()
                .map(supplier -> supplierMapper.toGetSuppliers(supplier)).toList();
    }

    public GetSupplierDetailRes getSupplierDetailRes(String supplierId){

        return supplierMapper.toGetSupplierDetailRes(supplierRepository
                .findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found")));
    }

    public CreateSupplierRes createSupplier(CreateSupplierReq request){

       var supplier = supplierMapper.toCreateSupplierReq(request);

       supplier = supplierRepository.save(supplier);

        return supplierMapper.toCreateSupplierRes(supplier);
    }

    public UpdateSupplierRes updateSupplier(String supplierId, UpdateSupplierReq request){

        log.warn("supplier_id_service: " + supplierId);


        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(()
                -> new RuntimeException("Supplier not found"));

        supplierMapper.toUpdateSupplierReq(supplier, request);

        return supplierMapper.toUpdateSupplierRes(supplierRepository.save(supplier));

    }

    public void deleteSupplier(String supplier_id){

        if(!supplierRepository.existsById(supplier_id)){
            throw new RuntimeException("Supplier not found");
        }

        supplierRepository.deleteById(supplier_id);
    }

}
