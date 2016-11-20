package abhi.com.mobitest.registeration;



/**
 * Created by Abhishek on 03-Nov-16.
 */
public abstract class RegistrationAbsFactory {
    public static IRegistrationManager getRegManager(RegistrationAbsFactory factory){
        return factory.createRegManager();
    }
    abstract IRegistrationManager createRegManager();
}
