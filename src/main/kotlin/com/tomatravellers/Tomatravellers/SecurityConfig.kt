package com.tomatravellers.Tomatravellers

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.tomatravellers.Tomatravellers.data.models.Role
import com.tomatravellers.Tomatravellers.presentation.respone.MetaResponse
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@EnableWebSecurity @Configuration
class SecurityConfig: WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var appProperties: AppPropeties
    //var filter: JwtAuthorizationFilter = JwtAuthorizationFilter("hello")

    //val tokenkey = appProperties.tokenSigningKey


    @Throws(Exception::class)
    override fun configure(http: HttpSecurity ) { //TODO make the diference between api and backofice

        var filter: JwtAuthorizationFilter = JwtAuthorizationFilter(appProperties.tokenSigningKey)
        http?.let {
            //print("TEST::"+SecurityContextHolder.getContext().authentication.principal)
            it.cors().and()
            .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/api/v1/oauth/**").permitAll()
                .antMatchers("/live").hasAuthority("ROLE_SUPERADMIN")
                .antMatchers("/api/v1/backoffice/**").hasAuthority("ROLE_SUPERADMIN")
            .anyRequest().authenticated()
                .and()
            .addFilterBefore(filter, BasicAuthenticationFilter::class.java)


        }
    }

    @Throws(java.lang.Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/api/v1/backoffice/oauth/**")
        web.ignoring().antMatchers("/")
        web.ignoring().antMatchers("/api/v1/code/**")
        web.ignoring().antMatchers("/api/v1/user/register")
        web.ignoring().antMatchers("/api/v1/oauth/**")
    }
}

class JwtAuthorizationFilter(test: String) : OncePerRequestFilter() {

    //private final val authenticationManager: AuthenticationManager = AuthenticationManager("sdf")
    @Autowired
    lateinit var appProperties: AppPropeties
    val token = test



    @Throws(Exception::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = request.getHeader("Authorization")
        try {

            val token = Jwts.parser().setSigningKey(token)
                .parseClaimsJws(jwt.replace("Bearer", ""))
            val userAuthorities = token.body.get("roles") as ArrayList<LinkedHashMap<String, Role>>

            var authorities = mutableListOf<SimpleGrantedAuthority>()

            userAuthorities.forEach {
                authorities.add(SimpleGrantedAuthority(it.get("name").toString()))
            }

            val access = UsernamePasswordAuthenticationToken("",null,authorities) //TODO put username from token here

             SecurityContextHolder.getContext().authentication = access
            filterChain.doFilter(request,response)

        }catch (e: SignatureException){
            manageErrors(response,e,"Error with Json autentification, not valid token",404)
        }catch (e: MalformedJwtException){
            manageErrors(response,e,"Error with Autentification Json format",404)
        }catch (e:ExpiredJwtException){
            manageErrors(response,e,"Error with Autentification Json: Expired token",404)
        }//catch (e: Exception){ //TODO this can be improved
            //manageErrors(response,e,"Another exception",404)
        //}
    }

    private fun manageErrors(response: HttpServletResponse,e:Exception,Message:String,code:Int){
        print("NO VALID TOKEN:$e")
        val res = ServletServerHttpResponse(response)
        res.setStatusCode(HttpStatus.UNAUTHORIZED)
        res.servletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        res.body.write(jsonMapper().writeValueAsString(MetaResponse(e.message,Message,code)).toByteArray())

    }

}
