<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Permissao Exclui"/> 

<title>Permiss&atilde;o Exclui</title>

 <table class="ui-tabs"  align="center" style="font-size:100%;">
 <tr bgcolor="Silver">
  <th>&nbsp; Nome &nbsp; </th>
  <th>&nbsp; Matricula &nbsp; </th>
  <th>&nbsp; Forum &nbsp;</th>
  <th></th>
 </tr>

<c:forEach items="${listPermitidos}" var="usu">	  	 
	<tr class="ui-button-icon-only"
		<c:if test="${!b}"> bgcolor="#dddddd" </c:if>
	/> 
<%-- 	<c:set property="b" value="${!b}"/> --%>
	<td>&nbsp; ${usu.nome_usu}</td>
	<td>&nbsp; ${usu.matricula_usu}</td>
	<td>&nbsp; ${usu.forumFk.descricao_forum}</td>
	<td>&nbsp;
		<form name="frm_exclui_permissao" method="GET" action="@{permissao_exclui()}" enctype="multipart/form-data">
			<img  src="/siga/css/famfamfam/icons/delete.png">
			<input type="hidden" name="matricula_proibida" value="${usu.matricula_usu}" /> &nbsp;
			<input type="submit" value="Exclui"/>
		</form>
	</td>
  </tr>
</c:forEach>
 </table>
 <br><br>
 <div style="position:relaive;left:50%;"> <h4 class="ui-widget" style="color:red;"> ${mensagem}</h4></div>
<a style="position: absolute;left:5%;" class="ui-state-hover" href="/sigapp/permissao_exclui">Continuar Excluindo</a>
<a style="position:absolute;left:15%;" class="ui-state-hover" href="/sigapp/">Voltar</a>