package at.htl.kochrezepte.entity;

import javax.persistence.*;

@Entity(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "im_id")
    private Long id;

    @Column(name = "im_img_in_byte")
    private byte[] imgInByte;

    public Image() {
    }

    public Image(byte[] imgInByte) {
        this.imgInByte = imgInByte;
    }

    public byte[] getImgInByte() {
        return imgInByte;
    }

    public void setImgInByte(byte[] imgInByte) {
        this.imgInByte = imgInByte;
    }

    public Long getId() {
        return id;
    }
}
