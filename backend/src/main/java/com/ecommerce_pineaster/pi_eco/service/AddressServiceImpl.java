package com.ecommerce_pineaster.pi_eco.service;

import com.ecommerce_pineaster.pi_eco.exception.ApiException;
import com.ecommerce_pineaster.pi_eco.exception.ResourceNotFoundException;
import com.ecommerce_pineaster.pi_eco.model.Address;
import com.ecommerce_pineaster.pi_eco.model.User;
import com.ecommerce_pineaster.pi_eco.payload.AddressDTO;
import com.ecommerce_pineaster.pi_eco.payload.MessageResponse.MessageResponse;
import com.ecommerce_pineaster.pi_eco.repository.AddressRepository;
import com.ecommerce_pineaster.pi_eco.repository.UserRepository;
import com.ecommerce_pineaster.pi_eco.util.AuthUtil;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO,User user) {

        Address address = modelMapper.map(addressDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        AddressDTO addressdto =  modelMapper.map(savedAddress, AddressDTO.class);

        return addressdto;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {

        List<Address> addressList = addressRepository.findAll();
        if(addressList.isEmpty()){
            throw  new ApiException("Address list is empty");
        }
        List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class)).collect(Collectors.toList());
        return  addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).
                orElseThrow(() -> new ResourceNotFoundException("address","addressId",addressId));

        AddressDTO addressDTO =  modelMapper.map(address,AddressDTO.class);
        return addressDTO;
    }

    @Override
    public List<AddressDTO> getAddressByUser(User user) {

        List<Address> userAddresses = user.getAddresses();
        if(userAddresses.isEmpty()){
            throw  new ApiException("No address found");
        }

        List<AddressDTO> addressDTOList = userAddresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class)).collect(Collectors.toList());
        return  addressDTOList;
    }

    @Override
    public AddressDTO updateAddressById(AddressDTO addressDTO, Long addressId) {

        Address address = modelMapper.map(addressDTO, Address.class);

        Address findAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("address","addressId",addressId));

        if(findAddress!=null){
            findAddress.setCity(address.getCity());
            findAddress.setCountry(address.getCountry());
            findAddress.setBuildingName(address.getBuildingName());
            findAddress.setStreet(address.getStreet());
            findAddress.setPincode(address.getPincode());
            findAddress.setState(address.getState());
        }
        Address updatedAddress = addressRepository.save(findAddress);


        AddressDTO updatedAddressDTO =  modelMapper.map(updatedAddress,AddressDTO.class);

        User user = findAddress.getUser();
         user.getAddresses().removeIf(a -> a.getAddressId().equals(addressId));
         user.getAddresses().add(updatedAddress);

        return updatedAddressDTO;



    }

    @Override
    public String deleteAddressById(Long addressId) {

        Address address =  addressRepository.findById(addressId).
                orElseThrow(() -> new ResourceNotFoundException("address","addressId",addressId));


        User user = address.getUser();
        user.getAddresses().removeIf(a -> a.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(address);

        return "Address is successfully deleted";
    }


}
