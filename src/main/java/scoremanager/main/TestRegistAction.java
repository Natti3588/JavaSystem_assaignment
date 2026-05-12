package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistAction extends Action {

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

    // ログイン中の教員と学校を取得
    HttpSession session = req.getSession();
    Teacher teacher = (Teacher) session.getAttribute("user");
    School school = teacher.getSchool();

    // フォームから検索条件を取得
    String entYearString = req.getParameter("f1");
    String classNum = req.getParameter("f2");
    String subjectCd = req.getParameter("f3");
    String countString = req.getParameter("f4");

    // エラー格納用Map
    Map<String, String> errors = new HashMap<>();

    // 検索ボタンが押されたかどうか（初回アクセス時は f1 が null）
    boolean isSubmitted = entYearString != null;

    // すべての項目が「未選択(0)」以外なら検索可能と判定
    boolean isValid = isSubmitted && !"0".equals(entYearString) && !"0".equals(classNum)
        && !"0".equals(subjectCd) && !"0".equals(countString);

    if (isValid) {
      // 数値に変換
      int entYear = Integer.parseInt(entYearString);
      int count = Integer.parseInt(countString);

      // 科目コードから科目情報を取得
      SubjectDao subjectDao = new SubjectDao();
      Subject subject = subjectDao.get(subjectCd, school);

      // 条件に合うテスト一覧を取得
      TestDao testDao = new TestDao();
      List<Test> tests = testDao.filter(entYear, classNum, subject, count, school);

      // 一覧と表示用情報を画面に渡す
      req.setAttribute("tests", tests);
      req.setAttribute("subject", subject); // 見出しに表示する科目名
      req.setAttribute("num", count); // 見出しに表示する回数

    } else if (isSubmitted) {
      // 検索ボタンが押されたが未選択項目がある場合のみエラー表示
      errors.put("filter", "入学年度とクラスと科目と回数を選択してください");
    }

    // 入学年度のプルダウン用リスト
    int year = LocalDate.now().getYear();
    List<Integer> entYearSet = new ArrayList<>();
    for (int i = year - 10; i <= year + 1; i++)
      entYearSet.add(i);

    // 回数のプルダウン用リスト
    List<String> numSet = new ArrayList<>();
    for (int i = 1; i <= 2; i++)
      numSet.add(String.valueOf(i));

    // 科目とクラスのプルダウン用リストをDaoから取得
    List<Subject> subjects = new SubjectDao().filter(school);
    List<String> classNumSet = new ClassNumDao().filter(school);

    // プルダウン用データを画面に渡す
    req.setAttribute("ent_year_set", entYearSet);
    req.setAttribute("class_num_set", classNumSet);
    req.setAttribute("num_set", numSet);
    req.setAttribute("subjects", subjects);

    // エラーと検索条件（selected維持用）を画面に渡す
    req.setAttribute("errors", errors);
    req.setAttribute("f1", entYearString);
    req.setAttribute("f2", classNum);
    req.setAttribute("f3", subjectCd);
    req.setAttribute("f4", countString);

    // 入力画面にフォワード
    req.getRequestDispatcher("test_regist.jsp").forward(req, res);
  }
}
