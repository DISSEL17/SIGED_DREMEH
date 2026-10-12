package pe.edu.utp.siged.bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

/** Utilidad para enviar mensajes a la vista (se muestran con h:messages / h:message). */
final class Mensajes {
    private Mensajes() { }

    static void info(String texto) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, texto, null));
    }

    static void error(String texto) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, null));
    }

    /** Conserva los mensajes tras una redireccion (faces-redirect=true). */
    static void conservar() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
}
