package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.ShipData;
import com.example.demo.ws.SendWebSocket;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShipService {


    /**
     * 全量数据-船航行信息，代替db  key:shipSign+record  value：map<record,List<ShipData>
     * record:出行记录 list:单此出行记录航行数据
     */
    private static final ConcurrentHashMap<String, Map<String, List<ShipData>>> shipPosition =
            new ConcurrentHashMap<>();


    /**
     * 接收航行数据，存放在本地,并发送至web端
     *
     * @param data 单个航行数据
     */
    public void receiveData(String data) throws IOException {
        ShipData shipData = JSONObject.parseObject(data, ShipData.class);
        String shipSign = shipData.getShipSign();
        String record = shipData.getRecord();
        if (shipPosition.containsKey(shipSign)) {
            if (shipPosition.get(shipSign).containsKey(record)) {
                shipPosition.get(shipSign).get(record).add(shipData);
            } else {
                List<ShipData> list = new ArrayList<>();
                list.add(shipData);
                shipPosition.get(shipSign).put(record, list);
            }
        } else {
            Map<String, List<ShipData>> map = new HashMap<>();
            List<ShipData> list = new ArrayList<>();
            list.add(shipData);
            map.put(record, list);
            shipPosition.put(shipSign, map);
        }
        SendWebSocket.sendInfo(data);
    }


    public List<ShipData> qryOneData(String shipSign, String record) {
        if (shipPosition.containsKey(shipSign)) {
            if (shipPosition.get(shipSign).containsKey(record)) {
                return shipPosition.get(shipSign).get(record);
            } else {
                System.out.println("there is not ship data shipSign：" + shipSign);
                return new ArrayList<>();
            }
        } else {
            System.out.println("there is not ship data shipSign：" + shipSign);
            return new ArrayList<>();
        }
    }

    /**
     * 获取单个船体全量航行数据
     *
     * @param shipSign 船体标记
     * @return
     */
    public Map<String, List<ShipData>> qryData(String shipSign) {
        if (shipPosition.containsKey(shipSign)) {
            return shipPosition.get(shipSign);
        } else {
            System.out.println("there is not ship data shipSign：" + shipSign);
            return new HashMap<>();
        }
    }


    public Map<String, Map<String, List<ShipData>>> qryAllData() {
        return shipPosition;

    }
}