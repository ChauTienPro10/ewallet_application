package com.wallet.web3j;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class MyContractWrapper {
    private final Web3j web3j;
    private final Credentials credentials;
    private final Test contract;

    public MyContractWrapper() {
        BigInteger gasPrice = BigInteger.valueOf(20000000000L);
        BigInteger gasLimit = BigInteger.valueOf(500000);
        ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
        web3j = Web3j.build(new HttpService("http://localhost:8545"));
        credentials = Credentials.create("0x268d7a27b0c3d963ee6612f5fdaa534c55fcc696a87002208dd674af88c6e54e");

        String contractAddress = "0x45cD598f0970C28ca3281a8219C40fbE07e488DF";
        contract = Test.load(contractAddress, web3j, credentials, gasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> transferETH(String to, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                "transferETH",
                Arrays.asList(new Address(to), new Uint256(weiValue)),
                Collections.emptyList()
        );

        return contract.transferETH(to, weiValue);
    }

    public static void main(String[] args) throws Exception {
        MyContractWrapper contractWrapper = new MyContractWrapper();
        String toAddress = "0xC37dceA614BD02c6769BbCd7928cF041428fde9C";
        BigInteger weiValue = new BigInteger("5000000000000000000"); // 1 ETH in Wei

        RemoteFunctionCall<TransactionReceipt> remoteCall = contractWrapper.transferETH(toAddress, weiValue);
        TransactionReceipt transactionReceipt = remoteCall.send();

        // Handle the transaction receipt
        // ...
    }
}