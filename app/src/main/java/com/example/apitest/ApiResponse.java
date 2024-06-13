package com.example.apitest;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "response", strict = false)
public class ApiResponse {
    @Element(name = "header", required = false)
    public Header header;

    @Element(name = "body", required = false)
    public Body body;

    public static class Header {
        @Element(name = "resultCode", required = false)
        public String resultCode;

        @Element(name = "resultMsg", required = false)
        public String resultMsg;
    }

    public static class Body {
        @Element(name = "numOfRows", required = false)
        public int numOfRows;

        @Element(name = "pageNo", required = false)
        public int pageNo;

        @Element(name = "totalCount", required = false)
        public int totalCount;

        @ElementList(name = "items", required = false)
        public List<Item> items;
    }

    @Root(name = "item", strict = false)
    public static class Item {
        @Element(name = "hmcNm", required = false)
        public String hospitalName;

        @Element(name = "locAddr", required = false)
        public String hospitalAddress;
    }
}
