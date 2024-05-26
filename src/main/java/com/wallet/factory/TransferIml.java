package com.wallet.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.History_tranfer;
import com.wallet.entitis.Member;
import com.wallet.entitis.Transaction_block;
import com.wallet.entitis.Transfer;
import com.wallet.repositories.MemberRepository;
import com.wallet.repositories.TransferRepository;

public class TransferIml implements Transaction {
	@Autowired TransferRepository transferRepository;
	@Autowired MemberRepository memberRepository;
	@Override
	public History_tranfer setHistory_transaction(Transaction_block transaction) {
		History_tranfer ht = new History_tranfer();
		Transfer tran = transferRepository.findByTransactioncode(transaction.getTransaction_code());
		 ht.setType("transfer");
         Member mem = memberRepository.findByMemberid(tran.getReceive_id());
         ht.setReceiver(mem.getFname() + " " + mem.getLname());
         ht.setTime(tran.getDate_time());
         ht.setContent(tran.getNote());
         ht.setAmount(tran.getTransfer_amount());
         return ht;
	}
	@Override
	public void get_repository(JpaRepository repo,JpaRepository member) {
			this.memberRepository=(MemberRepository) member;
			this.transferRepository=(TransferRepository) repo;
	}

}
