package com.diego.SpringSecutiry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration //Indicamos que es una clase de configuration
@EnableWebSecurity //Indicamos que pertenece a la seguridad de springSecurity
public class SecurityConfig {


    //Configuration One
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests()
                    .requestMatchers("/v1/index2").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .httpBasic()//Enviamos nuestro usuario y contraseña en el header de la header de la peticion sin necesidad de loguearmeKiri
                .and()
                .build();
        //SecurityFilterChain es una interfaz de SprSec-> para configurar la seguridad
        //El objeto httpSecurity es un bean que nos ayuda a configurar la seguridad
        //csrf --> Cross-Site-Request-Forgery, es el fomulario a traves del navegador, inabilitamos esa segurida
        // que trae por defecto springsecuity, se activa cuando trabajamos con formularios
        //authorizeHttpRequests --> Establezco cuales son las url que van a estar protegidas y las que no
        //requestMatchers son las peticiones que coincidan con los endpoints que se establezcan ahi, se colocan
        // las url que yo voy a permitir que no necesitan alguna autorizacion
        // .anyRequest().authenticated(), decimos que cualquier otra peticion requiere de una autenticacion
        // .and, me permite agregar mas configuraciones
        //.formLogin() decimos que este formulario por defecto este para todos
    }

    /*
    //Configuration Two
    @Bean
    public  SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth->{
                    auth.requestMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin()
                    .successHandler(successHandler()) //Puedo establecer a que endPoint quiero que me redirija
                    .permitAll()
                .and()
                .sessionManagement() //Nos sirve para configurar el comportamiento de sesiones
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)//Politica de nuestra sesion
                    .invalidSessionUrl("/login")//Si la sesion es invalida o no se logra autenticar a donde lo redirigimos
                    .maximumSessions(1)//Cual es el numero maximo de sesiones que tiene un usuario
                    .expiredUrl("/login")//me permite expirar la sesion a donde lo redirigimos
                .sessionRegistry(sessionRegistry())//Puedo inyectar un objeto que se encargue de administrar todos los registros que estan en la sesion
                .and()
                .sessionFixation()
                    .migrateSession()
                .and()
                .build();
    } */

    //La ventaja de trabajar con sesiones esque puedes guardar los datos del usuario
    //ALWAYS --> Crea una sesion siempre y cuando no exista ninguna, si existe la reutiliza
    //IF-REQUIRED --> Crea una nueva sesion solo si es necesario
    //NEVER --> No crea ninguna sesion, pero si existe la utiliza
    //STATELES --> No crea sesion ni trabaja con ninguna sesion, la sesion la trabaja de tipo independiente

    //migrateSession -> Esta estrategia, cuando se detecta que se esta intentando hacer un ataque de fijacion de sesion
    // spring crea un nuevo id de sesion, hace un copia y pega de los datos en esa nueva sesion
    //newSession -> Hace lo mismo que el migrateSession, pero no copia los datos
    // none -> deshabilita la seguirdad de sesion

    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication) -> {
            response.sendRedirect("/v1/session");
        });
    };

    @Bean
    public SessionRegistry sessionRegistry(){ //Me ayuda a obtener los datos de la sesion
        return new  SessionRegistryImpl();
    }
}
