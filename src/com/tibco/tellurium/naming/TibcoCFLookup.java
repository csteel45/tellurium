package com.FortMoon.tellurium.naming;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class FortMoonCFLookup implements javax.naming.spi.InitialContextFactory {

    @SuppressWarnings("unchecked")
	public Context getInitialContext(Hashtable environment) {

        try {

            environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.FortMoon.tibjms.naming.TibjmsInitialContextFactory");

            Context ctx = new InitialContext(environment);

            return new FortMoonContext(ctx);

        } catch (NamingException ne) {
            ne.printStackTrace();
            return null;
        }
    }
}
