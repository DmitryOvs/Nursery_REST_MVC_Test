package by.chukotka.nursery.util;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class H2databaseInitTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp(){
        jdbcTemplate.execute(createTablePlant);
        jdbcTemplate.execute(createTableVariety);
        jdbcTemplate.execute(addPlants);
        jdbcTemplate.execute(addVarieties);
    }

    @Value("${sql.script.create.table.plant}")
    protected String createTablePlant;

    @Value("${sql.script.create.table.variety}")
    protected String createTableVariety;

    @Value("${sql.script.add.plants}")
    protected String addPlants;

    @Value("${sql.script.add.varieties}")
    protected String addVarieties;

    @Value("${sql.script.drop.table.variety}")
    protected String dropTableVariety;

    @Value("${sql.script.drop.table.plant}")
    protected String dropTablePlant;



    @AfterEach
    public void tearDown(){
        jdbcTemplate.execute(dropTableVariety);
        jdbcTemplate.execute(dropTablePlant);

    }
}

