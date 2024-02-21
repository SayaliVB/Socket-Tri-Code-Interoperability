
import org.junit.*;

import gash.payload.BasicBuilder;
import gash.payload.Message;

public class BuilderTest {
	static final String n = "fred", g = "dogs", t = "hello";
	static final String msg = "dogs,fred,hello";

	@Test
	public void testBuilder() throws Exception {
		BasicBuilder builder = new BasicBuilder();

		String s = builder.encode(new Message(n,g,t));
		Assert.assertEquals(msg,s);

		System.out.println("msg: " + s);

		var m = builder.decode(s.getBytes());
		Assert.assertEquals(m.getName(),n);
		Assert.assertEquals(m.getGroup(), g);
		Assert.assertEquals(m.getText(),t);
	}
}