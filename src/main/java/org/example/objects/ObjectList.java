package org.example.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectList {

    private Set<GraphicObject> objects;

    public ObjectList() {
        objects = new HashSet<>();
    }

    public void generateFigure() {
        Random random = new Random();
        int x, y, width, height;
        int figureType = random.nextInt(3);

        width = random.nextInt(71) + 30;
        height = random.nextInt(71) + 30;
        x = random.nextInt(670 - width);
        y = random.nextInt(600 - height);

        if (figureType == 0) {
            objects.add(new Rectangle(x, y, width, height));
        } else if (figureType == 1) {
            objects.add(new Oval(x, y, width, height));
        } else {
            objects.add(new Triangle(
                    x +  ((random.nextInt(2) == 0) ? (random.nextInt(31) - 60) : (random.nextInt(31) + 30)),
                    y +  ((random.nextInt(2) == 0) ? (random.nextInt(31) - 60) : (random.nextInt(31) + 30)),
                    x +  ((random.nextInt(2) == 0) ? (random.nextInt(31) - 60) : (random.nextInt(31) + 30)),
                    y +  ((random.nextInt(2) == 0) ? (random.nextInt(31) - 60) : (random.nextInt(31) + 30)),
                    x +  ((random.nextInt(2) == 0) ? (random.nextInt(31) - 60) : (random.nextInt(31) + 30)),
                    y +  ((random.nextInt(2) == 0) ? (random.nextInt(31) - 60) : (random.nextInt(31) + 30))
            ));
        }

    }

    public void clear() {
        objects.clear();
    }

    public void add(Set<GraphicObject> set) {
        objects.addAll(set);
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

}
