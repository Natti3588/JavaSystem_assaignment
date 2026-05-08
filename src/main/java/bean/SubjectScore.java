package bean;

public class SubjectScore {

  private Student student; // 学生
  private Subject subject; // 科目
  private School school; // 学校
  private int point; // 特典
  private String classNum; // クラス番号
  
  /**
   * ゲッター・セッター
   */
  public Student getStudent() {
    return student;
  }
  
  public void setStudent(Student student) {
    this.student = student;
  }
  
  public Subject getSubject() {
    return subject;
  }
  
  public void setSubject(Subject subject) {
    this.subject = subject;
  }
  
  public School getSchool() {
    return school;
  }
  
  public void setSchool(School school) {
    this.school = school;
  }
  
  public int getPoint() {
    return point;
  }
  
  public void setPoint(int point) {
    this.point = point;
  }
  
  public String getClassNum() {
    return classNum;
}

  public void setClassNum(String classNum) {
    this.classNum = classNum;
}
}
