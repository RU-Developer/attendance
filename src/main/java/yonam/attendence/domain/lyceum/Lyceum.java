package yonam.attendence.domain.lyceum;

import lombok.Data;

import java.io.Serializable;

@Data
public class Lyceum implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String phone;
}
