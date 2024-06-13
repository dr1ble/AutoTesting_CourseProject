package task5.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private Integer id;
    private String first_name;
    private String last_name;
    private String email;
    private String avatar;
}