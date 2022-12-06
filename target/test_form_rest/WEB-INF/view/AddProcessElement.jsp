<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>

<c:forEach var="field" items="${fields}">

    ${field.name}

    <c:if test="${field.tryGetAsProperty() == null}">
        <input type="text"/>
    </c:if>

    <c:if test="${field.tryGetAsProperty() != null}">

        <c:if test="${field.tryGetAsProperty().tryGetAsMoney() != null}">
            <input type="text" placeholder="Sum in Rub"/>
        </c:if>

        <c:if test="${field.tryGetAsProperty().tryGetAsSEmployee() != null}">
            <input type="text" placeholder="Employee id"/>
        </c:if>

        <c:if test="${field.tryGetAsProperty().tryGetAsString() != null}">
            <input type="text" placeholder="text"/>
        </c:if>

        <c:if test="${field.tryGetAsProperty().tryGetAsFile() != null}">
            <input type="text" placeholder="text"/>
        </c:if>

        <c:if test="${field.tryGetAsProperty().tryGetAsDetailText() != null}">
            <input type="text" placeholder="text"/>
        </c:if>


        <c:if test="${field.tryGetAsProperty().tryGetAsList() != null}">
            <select id="test" class="textbox combo" name="distribute_type" style="width: 180px; height: 35px;">
                <c:forEach var="pair" items="${field.tryGetAsProperty().tryGetAsList().keyValueMap.keySet()}">
                    <option value="${field.tryGetAsProperty().tryGetAsList().getValue(pair)}">${field.tryGetAsProperty().tryGetAsList().getValue(pair)}</option>
                </c:forEach>
            </select>
        </c:if>

        <c:if test="${field.tryGetAsProperty().tryGetAsEList() != null}">
                <c:if test="${field.tryGetAsProperty().tryGetAsEList().code.equals(\"PROEKT\")}">
                    <select id="test53" class="textbox combo" name="distribute_type" style="width: 180px; height: 35px;">
                    <c:forEach var="element" items="${statusElements}">
                        <option value="${element.getName()}">${element.getName()}</option>
                    </c:forEach>
                    </select>
                </c:if>

            <c:if test="${field.tryGetAsProperty().tryGetAsEList().code.equals(\"PROEKT\") == false}">
                <select id="test3" class="textbox combo" name="distribute_type" style="width: 180px; height: 35px;">
                <c:forEach var="element" items="${bx24.getListElementsByBlockID(field.tryGetAsProperty().tryGetAsEList().linkBlockID, \"\")}">
                    <option value="${element.getName()}">${element.getName()}</option>
                </c:forEach>
                </select>
            </c:if>
        </c:if>
    </c:if>

    <br/><br/>
</c:forEach>

</body>
</html>