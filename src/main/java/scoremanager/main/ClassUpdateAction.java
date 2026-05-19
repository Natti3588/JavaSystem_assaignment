package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tool.Action;

public class ClassUpdateAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    // 変更したいクラス番号を取得
    String old = req.getParameter("num");
    req.setAttribute("oldnum", old);
    // class_update.jspにフォワード
    req.getRequestDispatcher("class_update.jsp").forward(req, res);
  }

}
