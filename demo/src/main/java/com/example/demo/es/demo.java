package com.example.demo.es;

import lombok.Data;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class demo {

    @Data
    class Book {
        Integer id;
        String name;
        Integer state;
        String address;
        Date birth;
        String type;
        Date start;
        Date end;
    }


    @Autowired
    RestHighLevelClient restHighLevelClient;  //注入RestHighLevelClient

    public Integer update(Book book) throws IOException {  //修改
        //      封装请求对象
        IndexRequest indexRequest = new IndexRequest("book", "doc", book.getId() + "");
        Map map = new HashMap();
        map.put("name", book.getName());
        map.put("state", book.getState());
        map.put("address", book.getAddress());
        map.put("birth", book.getBirth());
        map.put("type", book.getType());
        indexRequest.source(map);

        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        int status = response.status().getStatus();
        return status;
    }

    public Integer add(Book book) throws IOException {  //添加   BulkRequest bulkRequest=new BulkRequest();   // bulk:批量插入
//      构建搜索请求对象，需要指定索引库名称book
        //      封装请求对象
        IndexRequest indexRequest = new IndexRequest("book", "doc", book.getId() + "");
        Map map = new HashMap();
        map.put("name", book.getName());
        map.put("state", book.getState());
        map.put("address", book.getAddress());
        map.put("birth", book.getBirth());
        map.put("type", book.getType());
        indexRequest.source(map);

        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        int status = response.status().getStatus();
        return status;

    }

    public Integer delete(Integer id) throws IOException {  //删除
        //构建搜索请求对象，需要指定索引库名称book
        DeleteRequest deleteRequest = new DeleteRequest("book", "doc", id + "");
        //        发送请求，获取响应结果
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        return delete.status().getStatus();
    }

    public Book updateById(Integer id) throws IOException, ParseException {  //通过id修改
        //构建搜索请求对象，需要指定索引库名称book
        SearchRequest searchRequest = new SearchRequest("book");
        searchRequest.types("doc");

        //        请求体
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("_id", id + "");
        searchSourceBuilder.query(queryBuilder);
//        请求体放到请求对象中
        searchRequest.source(searchSourceBuilder);

//        发送请求，获取响应结果
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        Map<String, Object> asMap = hits[0].getSourceAsMap();

        Book book = new Book();
        book.setId(id);
        book.setName(asMap.get("name").toString());
        book.setState((Integer) asMap.get("state"));
        book.setAddress(asMap.get("address").toString());
        book.setType(asMap.get("type").toString());

        String birth = asMap.get("birth").toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse(birth);
        book.setBirth(parse);

        return book;

    }

    public List<Book> findAll(Book book) throws IOException, ParseException {  //查询
        //构建搜索请求对象，需要指定索引库名称book
        SearchRequest searchRequest = new SearchRequest("book");
        searchRequest.types("doc");

        //条件构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        MatchAllQueryBuilder allQueryBuilder = QueryBuilders.matchAllQuery();  //matchAllQuery 查询所有
        //布尔查询,将多个查询结合起来 must mustnot should , must should 类似sql中 and  or
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        String name = book.getName();
        String address = book.getAddress();
        Date start = book.getStart();
        Date end = book.getEnd();
        if (name != null && !"".equals(name)) {
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("name", name);  //matchQuery 模糊查询
            boolQueryBuilder.must(queryBuilder);
        }
        if (address != null && !"".equals(address)) {
            TermQueryBuilder queryBuilder = QueryBuilders.termQuery("address", address); // termQuery 等值查询，如果是不需要分词
            // (精确)查询，"address.keyword" 加上.keyword
            boolQueryBuilder.must(queryBuilder);
        }
        if (start != null) {
//            范围查询
            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("birth").gte(start);  // RangeQueryBuilder
            // 范围查询，可查区间
            boolQueryBuilder.must(rangeQueryBuilder);
        }
        if (end != null) {
//            范围查询
            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("birth").lte(end);
            boolQueryBuilder.must(rangeQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.sort("time", SortOrder.ASC);  //时间升序
//        请求体放到请求对象中
        //	分页
//        searchSourceBuilder.from(goodsReq.getPageNum() - 1);
//        searchSourceBuilder.size(goodsReq.getPageSize());
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();  //返回结果
        List<Book> bookList = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> asMap = hit.getSourceAsMap(); //返回结果也可以是 String sourceAsString = hit
            // .getSourceAsString();   Map map1 = JSON.parseObject(sourceAsString, Map.class); //可转成map或者实体
            Book book1 = new Book();
            book1.setId(Integer.parseInt(hit.getId()));
            book1.setName(asMap.get("name").toString());
            book1.setState((Integer) asMap.get("state"));
            book1.setAddress(asMap.get("address").toString());
            String birth = asMap.get("birth").toString();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = simpleDateFormat.parse(birth);
            book1.setBirth(parse);

            book1.setType(asMap.get("type").toString());
            bookList.add(book1);
        }
        return bookList;
    }
}
