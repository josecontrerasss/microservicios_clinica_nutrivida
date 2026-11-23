package cl.nutrivida.novedades.config;

import cl.nutrivida.novedades.model.Novedad;
import cl.nutrivida.novedades.repository.NovedadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final NovedadRepository repository;

    public DataInitializer(NovedadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            Novedad n1 = new Novedad();
            n1.setTitulo("Cuánta agua necesitas al día");
            n1.setResumen("La regla de los dos litros no aplica igual para todos. Estas son las señales que sí sirven.");
            n1.setImagen("/Imagenes/Novedades/Agua.jpg");
            n1.setParrafo1("La famosa regla de los dos litros diarios es solo un promedio. Tu necesidad real cambia según tu peso, cuánto te mueves, el clima y hasta lo que comes durante el día.");
            n1.setParrafo2("Una señal simple y confiable es el color de la orina: mientras más clara, mejor hidratado estás. Si es oscura y con olor fuerte, necesitas tomar más agua.");
            n1.setParrafo3("Las frutas y verduras también aportan líquido, así que no todo tiene que venir del vaso. Lo importante es repartir el consumo durante el día y no dejarlo todo para la noche.");

            Novedad n2 = new Novedad();
            n2.setTitulo("Colaciones entre comidas");
            n2.setResumen("Comer entre comidas no es el problema. El asunto es qué eliges y a qué hora.");
            n2.setImagen("/Imagenes/Novedades/colaciones-entre-comidas.jpg");
            n2.setParrafo1("Muchas personas llegan al almuerzo o a la cena con tanta hambre que terminan comiendo el doble de lo que necesitaban. Ahí es donde se descontrola el día.");
            n2.setParrafo2("Una buena colación combina algo que aporte energía con algo que aporte proteína. Una fruta con un puñado de frutos secos, un yogur natural o un huevo duro cumplen bien esa función.");
            n2.setParrafo3("Lo importante es que sea una porción pequeña y a una hora fija, no algo que se pique durante toda la tarde.");

            Novedad n3 = new Novedad();
            n3.setTitulo("Un desayuno que rinde");
            n3.setResumen("Qué incluir en la mañana para llegar sin hambre al almuerzo.");
            n3.setImagen("/Imagenes/Novedades/un-desayuno-que-rinde.jpg");
            n3.setParrafo1("El desayuno marca cómo te sientes las primeras horas del día. Si solo tomas café o comes algo muy dulce, es probable que a media mañana ya tengas hambre y te cueste concentrarte.");
            n3.setParrafo2("Un desayuno completo incluye tres cosas: una fuente de proteína como huevo, queso fresco o yogur, un carbohidrato integral como pan o avena, y algo de fruta.");
            n3.setParrafo3("No tiene que ser elaborado ni caro. Avena con fruta o pan integral con huevo funcionan perfecto y se preparan en cinco minutos.");

            Novedad n4 = new Novedad();
            n4.setTitulo("Cómo leer las etiquetas");
            n4.setResumen("Los sellos y la tabla nutricional explicados en simple.");
            n4.setImagen("/Imagenes/Novedades/como-leer-etiquetas.jpg");
            n4.setParrafo1("Los sellos negros indican que un producto es alto en azúcares, grasas saturadas, sodio o calorías. Sirven como primera señal, pero no reemplazan mirar la tabla nutricional.");
            n4.setParrafo2("En la tabla, revisa siempre a cuántos gramos corresponde la porción. Muchos envases muestran los valores de una porción mucho más chica de la que uno realmente come.");
            n4.setParrafo3("La lista de ingredientes va ordenada de mayor a menor cantidad. Si el azúcar aparece dentro de los tres primeros, el producto tiene bastante más de lo que parece.");

            repository.saveAll(List.of(n1, n2, n3, n4));
        }
    }
}