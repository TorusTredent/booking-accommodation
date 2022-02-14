package by.bookingaccommodation.controller;

import by.bookingaccommodation.entity.hotel.Hotel;
import by.bookingaccommodation.entity.hotel.Room;
import by.bookingaccommodation.service.HotelService;
import by.bookingaccommodation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class PreviewController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @GetMapping()
    public String preview(HttpSession session) {
        session.setAttribute("country", "Belarus");
        save();
        return "preview";
    }

    private void save() {
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://media.wired.com/photos/5fb70f2ce7b75db783b7012c/master/pass/Gear-Photos-597589287.jpg");
        imageUrls.add("https://cdn.pocket-lint.com/r/s/1200x/assets/images/151442-cameras-feature-stunning-photos-from-the-national-sony-world-photography-awards-2020-image1-evuxphd3mr.jpg");
        imageUrls.add("https://iso.500px.com/wp-content/uploads/2016/03/stock-photo-142984111.jpg");
        imageUrls.add("https://thumbs.dreamstime.com/b/environment-earth-day-hands-trees-growing-seedlings-bokeh-green-background-female-hand-holding-tree-nature-field-gra-130247647.jpg");
        IntStream.range(0, 20).mapToObj(i -> new Hotel("Name", "Belarus", "Minsk", "Telmana", "53/3", "+375336405535", 3, imageUrls)).forEachOrdered(hotel -> {hotelService.save(hotel);});
        IntStream.range(0, 20).forEachOrdered(i -> roomService.save(new Room(i, 12)));
    }

}
