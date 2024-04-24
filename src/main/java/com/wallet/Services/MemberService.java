package com.wallet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.wallet.repositories.MemberRepository;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepository;
	
	
	
}
