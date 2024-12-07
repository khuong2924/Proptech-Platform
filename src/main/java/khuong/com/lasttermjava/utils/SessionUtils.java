//package khuong.com.lasttermjava.utils;
//
//import jakarta.servlet.http.HttpSession;
//import khuong.com.webmakeupconnection.entity.User;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//@Component
//public class SessionUtils {
//
//    public static Long getCurrentUserId() {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (requestAttributes != null) {
//            HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
//            if (session != null) {
//                User user = (User) session.getAttribute("user");
//                return user != null ? user.getId() : null;
//            }
//        }
//        return null;
//    }
//
//    public static User getCurrentUser() {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (requestAttributes != null) {
//            HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
//            if (session != null) {
//                User user = (User) session.getAttribute("user");
//                return user;
//            }
//        }
//        return null;
//    }
//}
