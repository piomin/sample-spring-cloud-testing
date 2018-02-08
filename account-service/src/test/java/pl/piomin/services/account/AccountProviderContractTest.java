package pl.piomin.services.account;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;

@RunWith(PactRunner.class)
@Provider("customerServiceProvider")
@PactBroker(host = "192.168.99.100", port = "9080")
public class AccountProviderContractTest {

	private static ConfigurableApplicationContext application;
	
	@TestTarget
	public final Target target = new HttpTarget(8091);

	@BeforeClass
	public static void startSpring() {
		new SpringApplicationBuilder(AccountApplication.class).profiles("no-discovery", "test").build().run();
	}

    @State("list-of-3-accounts")
    public void toDefaultState() {
        System.out.println("Now service in default state");
    }
    
	@AfterClass
	public static void kill() {
		application.stop();
	}
	
}
