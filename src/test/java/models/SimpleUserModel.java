package models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;

@Table(name = "user")
public class SimpleUserModel extends StormModel {

    @Column
    private String userName;

    @Column
    private Integer score;

    @Column(name = "email")
    private String emailAddress;

}
