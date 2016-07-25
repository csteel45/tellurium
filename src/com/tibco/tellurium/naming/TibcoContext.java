package com.FortMoon.tellurium.naming;


import com.FortMoon.tibjms.TibjmsConnectionFactory;
import com.FortMoon.tibjms.naming.TibjmsContext;

import java.util.Hashtable;

import javax.jms.ConnectionFactory;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class FortMoonContext extends java.lang.Object implements javax.naming.Context {

    private Context FortMoonCtx = null;

    public FortMoonContext(Context ctx) {
        FortMoonCtx = ctx;
    }

    public Object lookup(String factoryName) {
        Object obj = null;;
        try {
            obj = FortMoonCtx.lookup(factoryName);
            if (obj instanceof TibjmsConnectionFactory) {
                ConnectionFactory cf = (ConnectionFactory)obj;

                Hashtable<?,?> env = FortMoonCtx.getEnvironment();

                String user = (String)env.get(Context.SECURITY_PRINCIPAL);
                String password = (String)env.get(Context.SECURITY_CREDENTIALS);
                String ssl_vendor = (String)env.get(TibjmsContext.SSL_VENDOR);
                String ssl_identity = (String)env.get(TibjmsContext.SSL_IDENTITY);
                String ssl_password = (String)env.get(TibjmsContext.SSL_PASSWORD);
		String ssl_trusted_file = (String)env.get(TibjmsContext.SSL_TRUSTED_CERTIFICATES);

                System.out.println("============getting environment values=================");
                System.out.println("user = " + user );
                System.out.println("password = " + password );
                System.out.println("ssl_vendor = "+ ssl_vendor);
                System.out.println("ssl_identity = " + ssl_identity );
                System.out.println("ssl_password = "+ ssl_password);
                System.out.println("ssl_trusted_file = "+ ssl_trusted_file);
                System.out.println("============getting environment values=================");

                ((TibjmsConnectionFactory)cf).setUserName(user);
                ((TibjmsConnectionFactory)cf).setUserPassword(password);
                ((TibjmsConnectionFactory)cf).setSSLIdentity(ssl_identity);
                ((TibjmsConnectionFactory)cf).setSSLPassword(ssl_password);
                ((TibjmsConnectionFactory)cf).setSSLVendor(ssl_vendor);
                ((TibjmsConnectionFactory)cf).setSSLTrustedCertificate(ssl_trusted_file);
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Object lookup(Name name) throws NamingException {
            return FortMoonCtx.lookup(name);
    }

    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        throw new NamingException("some text");
    }

    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        throw new NamingException("some text");
    }

    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
       throw new NamingException("some text");
    }

    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
       throw new NamingException("some text");
    }

    public void destroySubcontext(Name name) throws NamingException {
        FortMoonCtx.destroySubcontext(name);
    }

    public void destroySubcontext(String name) throws NamingException {
        FortMoonCtx.destroySubcontext(name);
    }

    public Context createSubcontext(Name name) throws NamingException {
        return FortMoonCtx.createSubcontext(name);
    }

    public Context createSubcontext(String name) throws NamingException {
        return FortMoonCtx.createSubcontext(name);
    }

    public Object lookupLink(Name name) throws NamingException {
        return FortMoonCtx.lookupLink(name);
    }

    public Object lookupLink(String name) throws NamingException {
        return FortMoonCtx.lookupLink(name);
    }

    public NameParser getNameParser(Name name) throws NamingException {
        return FortMoonCtx.getNameParser(name);
    }

    public NameParser getNameParser(String name) throws NamingException {
        return FortMoonCtx.getNameParser(name);
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
        return FortMoonCtx.composeName(name, prefix);
    }

    public String composeName(String name, String prefix) throws NamingException {
        return FortMoonCtx.composeName(name, prefix);
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return FortMoonCtx.addToEnvironment(propName, propVal);
    }

    public Object removeFromEnvironment(String propName) throws NamingException {
        return FortMoonCtx.removeFromEnvironment(propName);
    }

    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return FortMoonCtx.getEnvironment();
    }

    public void close() throws NamingException {
        FortMoonCtx.close();
    }

    public String getNameInNamespace() throws NamingException {
        return FortMoonCtx.getNameInNamespace();
    }

    public void bind(Name name, Object obj) throws NamingException {
        FortMoonCtx.bind(name, obj);
    }

    public void bind(String name, Object obj) throws NamingException {
        FortMoonCtx.bind(name, obj);
    }

    public void rebind(Name name, Object obj) throws NamingException {
        FortMoonCtx.rebind(name, obj);
    }

    public void rebind(String name, Object obj) throws NamingException {
        FortMoonCtx.rebind(name, obj);
    }

    public void unbind(Name name) throws NamingException {
        FortMoonCtx.unbind(name);
    }

    public void unbind(String name) throws NamingException {
        FortMoonCtx.unbind(name);
    }

    public void rename(Name oldName, Name newName) throws NamingException {
        FortMoonCtx.rename(oldName, newName);
    }

    public void rename(String oldName, String newName) throws NamingException {
        FortMoonCtx.rename(oldName, newName);
    }

}
