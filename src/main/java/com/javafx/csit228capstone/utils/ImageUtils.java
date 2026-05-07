package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.User;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import java.io.InputStream;

public class ImageUtils {

    public static void makeRounded(ImageView imageView) {
        // 1. Explicitly set a square size (e.g., 100x100)
        // You can change 100 to whatever size you want the circle to be
        double diameter = 100.0;

        imageView.setFitWidth(diameter);
        imageView.setFitHeight(diameter);
        imageView.setPreserveRatio(false); // We force it to be square

        // 2. Create the clip perfectly centered
        Circle clip = new Circle(diameter / 2, diameter / 2, diameter / 2);
        imageView.setClip(clip);
    }

    // Loads image from the main 'users' table
    public static void loadProfileImage(int userId, ImageView imageView) {
        InputStream is = UserDAO.getUserProfilePicture(userId);
        if (is != null) {
            imageView.setImage(new Image(is));
        } else {
            // Default icon if no image exists
            imageView.setImage(new Image(ImageUtils.class.getResourceAsStream("/images/profile_icon.png")));
        }
        makeRounded(imageView);
    }

    // Loads image from the 'users_update' table
    public static void loadUpdateImage(int userId, ImageView imageView) {
        InputStream is = UserDAO.getUpdateProfilePicture(userId);
        if (is != null) {
            imageView.setImage(new Image(is));
            makeRounded(imageView);
        } else {
            // If no update image, fall back to the original
            loadProfileImage(userId, imageView);
        }
    }
}