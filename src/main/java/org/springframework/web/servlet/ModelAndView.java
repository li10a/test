package org.springframework.web.servlet;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public class ModelAndView {


    private Object view;

    private ModelMap model;

    private HttpStatus status;

    private boolean cleared = false;

    public ModelAndView() {
    }

    public ModelAndView(String viewName) {
        this.view = viewName;
    }

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView(String viewName, @Nullable Map<String, ?> model){
        this.view = viewName;
        if (model != null) {
            getModelMap().addAllAttributes(model);
        }
    }

    public ModelAndView(View view, @Nullable Map<String, ?> model) {
        this.view = view;
        if (model != null) {
            getModelMap().addAllAttributes(model);
        }
    }

    public ModelAndView(String view, HttpStatus status) {
        this.view = view;
        this.status = status;
    }

    public ModelAndView(@Nullable String viewName, @Nullable Map<String, ?> model, @Nullable HttpStatus status) {
        this.view = viewName;
        if (model != null) {
            getModelMap().addAllAttributes(model);
        }
        this.status = status;
    }

    /**
     * Set a view name for this ModelAndView, to be resolved by the
     * DispatcherServlet via a ViewResolver. Will override any
     * pre-existing view name or View.
     */
    public void setViewName(@Nullable String viewName) {
        this.view = viewName;
    }

    /**
     * Return the view name to be resolved by the DispatcherServlet
     * via a ViewResolver, or {@code null} if we are using a View object.
     */
    @Nullable
    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    /**
     * Set a View object for this ModelAndView. Will override any
     * pre-existing view name or View.
     */
    public void setView(@Nullable View view) {
        this.view = view;
    }

    /**
     * Return the View object, or {@code null} if we are using a view name
     * to be resolved by the DispatcherServlet via a ViewResolver.
     */
    @Nullable
    public View getView() {
        return (this.view instanceof View ? (View) this.view : null);
    }

    /**
     * Indicate whether or not this {@code ModelAndView} has a view, either
     * as a view name or as a direct {@link View} instance.
     */
    public boolean hasView() {
        return (this.view != null);
    }

    /**
     * Return whether we use a view reference, i.e. {@code true}
     * if the view has been specified via a name to be resolved by the
     * DispatcherServlet via a ViewResolver.
     */
    public boolean isReference() {
        return (this.view instanceof String);
    }

    /**
     * Return the model map. May return {@code null}.
     * Called by DispatcherServlet for evaluation of the model.
     */
    @Nullable
    protected Map<String, Object> getModelInternal() {
        return this.model;
    }

    public ModelAndView(String viewName, String modelName, Object modelObject) {
        this.view = viewName;
        addObject(modelName, modelObject);
    }

    public ModelAndView(View view, String modelName, Object modelObject) {
        this.view = view;
        addObject(modelName, modelObject);
    }

    /**
     * Return the model map. Never returns {@code null}.
     * To be called by application code for modifying the model.
     */
    public Map<String, Object> getModel() {
        return getModelMap();
    }

    /**
     * Set the HTTP status to use for the response.
     * <p>The response status is set just prior to View rendering.
     * @since 4.3
     */
    public void setStatus(@Nullable HttpStatus status) {
        this.status = status;
    }

    /**
     * Return the configured HTTP status for the response, if any.
     * @since 4.3
     */
    @Nullable
    public HttpStatus getStatus() {
        return this.status;
    }

    /**
     * Add an attribute to the model using parameter name generation.
     * @param attributeValue the object to add to the model (never {@code null})
     * @see ModelMap#addAttribute(Object)
     * @see #getModelMap()
     */
    public ModelAndView addObject(Object attributeValue) {
        getModelMap().addAttribute(attributeValue);
        return this;
    }

    /**
     * Add all attributes contained in the provided Map to the model.
     * @param modelMap a Map of attributeName -> attributeValue pairs
     * @see ModelMap#addAllAttributes(Map)
     * @see #getModelMap()
     */
    public ModelAndView addAllObjects(@Nullable Map<String, ?> modelMap) {
        getModelMap().addAllAttributes(modelMap);
        return this;
    }

    public void clear() {
        this.view = null;
        this.model = null;
        this.cleared = true;
    }

    /**
     * Return whether this ModelAndView object is empty,
     * i.e. whether it does not hold any view and does not contain a model.
     */
    public boolean isEmpty() {
        return (this.view == null && CollectionUtils.isEmpty(this.model));
    }

    /**
     * Return whether this ModelAndView object is empty as a result of a call to {@link #clear}
     * i.e. whether it does not hold any view and does not contain a model.
     * <p>Returns {@code false} if any additional state was added to the instance
     * <strong>after</strong> the call to {@link #clear}.
     * @see #clear()
     */
    public boolean wasCleared() {
        return (this.cleared && isEmpty());
    }

    /**
     * Return diagnostic information about this model and view.
     */
    @Override
    public String toString() {
        return "ModelAndView [view=" + formatView() + "; model=" + this.model + "]";
    }

    private String formatView() {
        return isReference() ? "\"" + this.view + "\"" : "[" + this.view + "]";
    }
    public ModelAndView addObject(String attributeName, @Nullable Object attributeValue) {
        getModelMap().addAttribute(attributeName, attributeValue);
        return this;
    }

    public ModelMap getModelMap() {
        if (this.model == null) {
            this.model = new ModelMap();
        }
        return this.model;
    }
}
