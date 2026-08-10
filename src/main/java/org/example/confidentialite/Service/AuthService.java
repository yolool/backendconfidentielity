package org.example.confidentialite.Service;

import lombok.AllArgsConstructor;
import org.example.confidentialite.Dto.LoginResDto;
import org.example.confidentialite.Entity.Personnel;
import org.example.confidentialite.Repository.PersonnelRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@AllArgsConstructor
public class AuthService implements UserDetailsService {


    private final PersonnelRepo personnelRepo;


    @Override
    public UserDetails loadUserByUsername(String idpersonnel)
            throws UsernameNotFoundException {


        Personnel personnel = personnelRepo.findById(idpersonnel)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Personnel not found : " + idpersonnel
                        ));


        return User.builder()
                .username(personnel.getIdPersonnel())

                .password("")

                .authorities(
                        Collections.singleton(
                                new SimpleGrantedAuthority(
                                       personnel.getDepartment()
                                )
                        )
                )

                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)

                .build();
    }



    public LoginResDto login(String idpersonnel , String Dep){

        Personnel personnel = personnelRepo.findByIdInDep(idpersonnel,Dep)
                .orElseThrow(() ->
                        new UsernameNotFoundException(idpersonnel));


        return new LoginResDto(
                personnel.getIdPersonnel()

        );
    }

}