package com.example.yofficial;

import java.util.ArrayList;
import java.util.List;

public class BoardItem {

    String board_id;
    String board_title;
    String board_uploader;
    String board_date;
    String board_data;



    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public String getBoard_title() {
        return board_title;
    }

    public void setBoard_title(String board_title) {
        this.board_title = board_title;
    }

    public String getBoard_uploader() {
        return board_uploader;
    }

    public void setBoard_uploader(String board_uploader) {
        this.board_uploader = board_uploader;
    }

    public String getBoard_date() {
        return board_date;
    }

    public void setBoard_date(String board_date) {
        this.board_date = board_date;
    }

    public String getBoard_data() {
        return board_data;
    }

    public void setBoard_data(String board_data) {
        this.board_data = board_data;
    }


    public BoardItem (String _board_id, String  _board_title, String _board_uploader, String _upload_date, String _board_data){
        board_id = _board_id;
        board_title= _board_title;
        board_uploader = _board_uploader;
        board_date = _upload_date;
        board_data = _board_data;
    }

    public BoardItem(){

    }

}
