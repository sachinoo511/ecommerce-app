package com.ecommerce_pineaster.pi_eco.controller;


import com.ecommerce_pineaster.pi_eco.model.Address;
import com.ecommerce_pineaster.pi_eco.model.User;
import com.ecommerce_pineaster.pi_eco.payload.AddressDTO;
import com.ecommerce_pineaster.pi_eco.payload.CartDTO;
import com.ecommerce_pineaster.pi_eco.repository.AddressRepository;
import com.ecommerce_pineaster.pi_eco.service.AddressService;
import com.ecommerce_pineaster.pi_eco.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO) {

        User user = authUtil.loggedInUser();

        AddressDTO   saveAddressDTO =  addressService.createAddress(addressDTO, user);

        return new ResponseEntity<AddressDTO>(saveAddressDTO, HttpStatus.CREATED);

    }


    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {

        List<AddressDTO> addressDTOList = addressService.getAllAddresses();
        return new ResponseEntity<>(addressDTOList,HttpStatus.FOUND);
    }


    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAllAddresses(@PathVariable Long addressId) {

        AddressDTO addressById = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressById,HttpStatus.FOUND);
    }
    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressByUser() {
    User user = authUtil.loggedInUser();
        List<AddressDTO> addressById = addressService.getAddressByUser(user);

        return new ResponseEntity<>(addressById,HttpStatus.FOUND);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@RequestBody AddressDTO addressDTO, @PathVariable Long addressId) {
        AddressDTO updatedAddress = addressService.updateAddressById(addressDTO,addressId);

        return new ResponseEntity<>(updatedAddress,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressById( @PathVariable Long addressId) {

        String  deleteAddress = addressService.deleteAddressById(addressId);

        return new ResponseEntity<>(deleteAddress,HttpStatus.OK);
    }


}
