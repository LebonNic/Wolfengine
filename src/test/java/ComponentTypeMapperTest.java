import com.arvernistudio.wolfengine.Component;
import com.arvernistudio.wolfengine.ComponentTypeMapper;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ComponentTypeMapperTest {

    private class FooComponent extends Component{

    }
    private class BarComponent extends Component{

    }
    private class BazComponent extends Component{

    }

    @Before
    public void setUp(){
        //Avoid NullPointerException when Gdx.app.log is called in the tested methods
        Gdx.app = Mockito.mock(Application.class);
    }

    @Test
    public void singletonInstantiationTest(){
        ComponentTypeMapper componentTypeMapper = ComponentTypeMapper.getInstance();
        assertNotNull(componentTypeMapper);
    }

    @Test
    public void indexGenerationTest(){
        int indexForFooComponent  = ComponentTypeMapper.getInstance()
                                        .getComponentIndex(FooComponent.class);
        int indexForBarComponent = ComponentTypeMapper.getInstance()
                                        .getComponentIndex(BarComponent.class);
        int indexForBazComponent = ComponentTypeMapper.getInstance()
                                        .getComponentIndex(BazComponent.class);

        assertEquals(indexForFooComponent, 1);
        assertEquals(indexForBarComponent, 2);
        assertEquals(indexForBazComponent, 3);
    }

    @Test
    public void indexConservationTest(){
        int indexOnGeneration  = ComponentTypeMapper.getInstance()
                .getComponentIndex(FooComponent.class);
        int indexOnSecondCall  = ComponentTypeMapper.getInstance()
                .getComponentIndex(FooComponent.class);

        assertEquals(indexOnGeneration, 1);
        assertEquals(indexOnSecondCall, 1);
    }
}
