package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.User;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import java.io.InputStream;

public class ImageUtils {

    public static void makeRounded(ImageView imageView) {
        double diameter = 100.0;

        imageView.setFitWidth(diameter);
        imageView.setFitHeight(diameter);
        imageView.setPreserveRatio(false);

        Circle clip = new Circle(diameter / 2, diameter / 2, diameter / 2);
        imageView.setClip(clip);
    }

    public static void loadProfileImage(int userId, ImageView imageView) {
        InputStream is = UserDAO.getUserProfilePicture(userId);
        if (is != null) {
            imageView.setImage(new Image(is));
        } else {

            imageView.setImage(new Image(ImageUtils.class.getResourceAsStream("/images/profile_icon.png")));
        }
        makeRounded(imageView);
    }

    public static void loadUpdateImage(int userId, ImageView imageView) {
        InputStream is = UserDAO.getUpdateProfilePicture(userId);
        if (is != null) {
            imageView.setImage(new Image(is));
            makeRounded(imageView);
        } else {

            loadProfileImage(userId, imageView);
        }
    }
}