package me.quyen.datainit;

import lombok.RequiredArgsConstructor;
import me.quyen.entities.Menu;
import me.quyen.entities.MenuItem;
import me.quyen.entities.Restaurant;
import me.quyen.repositories.MenuItemRepository;
import me.quyen.repositories.MenuRepository;
import me.quyen.repositories.RestaurantRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class DataInitialization {
    final MenuRepository menuRepository;
    final MenuItemRepository menuItemRepository;
    final RestaurantRepository restaurantRepository;
    @Bean
    @Transactional(
            propagation = Propagation.REQUIRES_NEW
            ,isolation = Isolation.SERIALIZABLE
    )
    public ApplicationRunner initData(){
        return args -> {
            //INSERT INTO `menu` VALUES (1,1,""),(2,2,"");
            Menu m1 = Menu.builder().restaurantId(1L)
                    .active(ThreadLocalRandom.current().nextBoolean())
                    .build();
            Menu m2 = Menu.builder().restaurantId(2L)
                    .active(ThreadLocalRandom.current().nextBoolean())
                    .build();
            menuRepository.saveAll(Set.of(m1,m2));
            //INSERT INTO `menu_item` VALUES
            // (1L,"Amritsari Dal","Green gram and bengal gram with spices","VEG","MAIN_COURSE",190.00)),
            // (1L,"Samosa Masala","Deep fried savory pastry with dressing of chickpeas salad","VEG","STARTER",210.00),
            // (1L,"Phirni","Creamy dessert of ground rice","VEG","DESSERT",120.00),
            // (2L,"Nizami Chicken Biryani","Freshly cooked kacchi dum ki biryany","NON_VEG","MAIN_COURSE",319.00),
            // (2L,"Mutton Seekh Kebab","Minced goat meet mixed with spices and cooked on gridle","NON_VEG","STARTER",365.00),
            // (2l,"Kurbaani Ka Meetha","Sweet dessert made from dried apricots and sugar enriched with saffron strands and rose water  ","VEG","DESSERT",160.00);
            menuItemRepository.saveAll(Set.of(
                    new MenuItem(1L,"Amritsari Dal","Green gram and bengal gram with spices","VEG","MAIN_COURSE",
                            BigDecimal.valueOf(190.00)),
                    new MenuItem(1L,"Samosa Masala","Deep fried savory pastry with dressing of chickpeas salad","VEG"
                            ,"STARTER", BigDecimal.valueOf(210.00)),
                    new MenuItem(1L,"Phirni","Creamy dessert of ground rice","VEG","DESSERT",
                            BigDecimal.valueOf(120.00)),
                    new MenuItem(2L,"Nizami Chicken Biryani","Freshly cooked kacchi dum ki biryany","NON_VEG",
                            "MAIN_COURSE",BigDecimal.valueOf(319.00)),
                    new MenuItem(2L,"Mutton Seekh Kebab","Minced goat meet mixed with spices and cooked on gridle",
                            "NON_VEG","STARTER",BigDecimal.valueOf(365.00)),
                    new MenuItem(2L,"Kurbaani Ka Meetha","Sweet dessert made from dried apricots and sugar enriched " +
                            "with saffron strands and rose water  ","VEG","DESSERT",BigDecimal.valueOf(160.00))
            ));
//            INSERT INTO `restaurant` VALUES 
//            (1,"Imli","Bangalore","VEG"),
//            (2,"Paradise","Hyderabad","NON_VEG"),
//            (1027,"Kritunga","Hyderabad","NON_VEG");
            restaurantRepository.saveAll(Set.of(
                    new Restaurant("Imli","Bangalore","VEG"),
                    new Restaurant("Paradise","Hyderabad","NON_VEG"),
                    new Restaurant("Kritunga","Hyderabad","NON_VEG")
            ));
        };
    }
}
