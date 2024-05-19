package com.wallet.web3j;

import org.web3j.abi.datatypes.Address;

import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;

import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import org.web3j.tx.gas.ContractGasProvider;

import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.protocol.core.DefaultBlockParameterName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class MyContractWrapper {
    private final  Web3j web3j;
    private final Credentials credentials;
    private final Test contract;
    private static final BigDecimal ETHER_TO_VND_RATE = new BigDecimal("7372610500");
    private static final BigDecimal VND_TO_ETHER_RATE = new BigDecimal("0.0000000001356371667810201");


    
    public MyContractWrapper(String key) {
        BigInteger gasPrice = BigInteger.valueOf(20000000000L);
        BigInteger gasLimit = BigInteger.valueOf(500000);
        ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
        web3j = Web3j.build(new HttpService("http://localhost:8545"));
//        credentials = Credentials.create("0x268d7a27b0c3d963ee6612f5fdaa534c55fcc696a87002208dd674af88c6e54e");
        credentials = Credentials.create(key);
        String contractAddress = "0x4b9f581ADaC19baB127F0DFA114034dA64327085";
        contract = Test.load(contractAddress, web3j, credentials, gasProvider);
    }
    


    public RemoteFunctionCall<TransactionReceipt> transferETH(String to, BigInteger weiValue) {
//        @SuppressWarnings("unused")
		final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                "transferETH",
                Arrays.asList(new Address(to), new Uint256(weiValue)),
                Collections.emptyList()
        );
        return contract.transferETH(to, weiValue);
    }
    
    public  boolean isPrivateKeyValid(String privateKeyHex, String address) {
        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKeyHex));
        Credentials credentials = Credentials.create(keyPair);
        String computedAddress = credentials.getAddress();

        return computedAddress.equalsIgnoreCase(address);
    }
    public  BigDecimal getAccountBalance(String address) {
        try {
            EthGetBalance balanceResponse = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            BigInteger balanceWei = balanceResponse.getBalance();
            BigDecimal balanceEther = Convert.fromWei(balanceWei.toString(), Convert.Unit.ETHER);
            return balanceEther;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    public static BigDecimal convertEtherToVnd(BigDecimal etherAmount) {
        return etherAmount.multiply(ETHER_TO_VND_RATE);
    }
    public static BigDecimal convertVndToEther(BigDecimal vndAmount) {
        return vndAmount.multiply(VND_TO_ETHER_RATE);
    }

    public static void main(String[] args) throws Exception {
        MyContractWrapper contractWrapper = new MyContractWrapper("0x268d7a27b0c3d963ee6612f5fdaa534c55fcc696a87002208dd674af88c6e54e");
//        String toAddress = "0xC37dceA614BD02c6769BbCd7928cF041428fde9C";
//        BigInteger weiValue = new BigInteger("5000000000000000000"); // 1 ETH in Wei
//
//        RemoteFunctionCall<TransactionReceipt> remoteCall = contractWrapper.transferETH(toAddress, weiValue);
//        TransactionReceipt transactionReceipt = remoteCall.send();
        BigDecimal balance = contractWrapper.getAccountBalance("0x09283041db45446836D18180a61943fBeA403C72");
        System.out.println("Số dư của địa chỉ  là: " + balance + " Ether");
        BigDecimal vndAmount_ = new BigDecimal("10000"); 

        BigDecimal etherAmount_ = convertVndToEther(vndAmount_);
        System.out.println(vndAmount_ + " VND tương đương với " + etherAmount_ + " Ether");
        BigDecimal etherAmount = new BigDecimal("1.5");
        
        
        BigDecimal vndAmount = convertEtherToVnd(etherAmount_);
        
        System.out.println(etherAmount + " Ether tương đương với " + vndAmount + " VND");
       

     
    }
}