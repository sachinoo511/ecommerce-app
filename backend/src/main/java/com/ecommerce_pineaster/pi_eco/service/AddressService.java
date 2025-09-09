package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.model.Address;
import com.ecommerce_pineaster.pi_eco.model.User;
import com.ecommerce_pineaster.pi_eco.payload.AddressDTO;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAddressByUser(User user);

    AddressDTO updateAddressById(AddressDTO addressDTO, Long addressId);

    String deleteAddressById(Long addressId);
}
