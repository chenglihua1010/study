package com.example.demo.comtroller;


import com.example.demo.dto.ShipData;
import com.example.demo.service.ShipService;
import com.example.demo.ws.ReceiveWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
public class ShipController {

    @Autowired
    ReceiveWebSocket webSocket;

    @Autowired
    private ShipService shipService;


    /**
     * 获取单个船体、单次航信数据
     *
     * @param sign   船体标记
     * @param record 航行记录
     * @return 航行数据
     */
    @GetMapping("/qryShipOneData")
    public List<ShipData> qryShipData(String sign, String record) {
        return shipService.qryOneData(sign, record);
    }

    /**
     * 获取单个船体全量数据
     *
     * @param sign 船体标记
     * @return 航行数据
     */
    @GetMapping("/qryShipData")
    public Map<String, List<ShipData>> qryShipData(String sign) {
        return shipService.qryData(sign);
    }

    /**
     * 获取全量数据
     *
     * @return 全量航行数据
     */
    @GetMapping("/qryAllData")
    public Map<String, Map<String, List<ShipData>>> qryAllData() {
        return shipService.qryAllData();
    }

}
