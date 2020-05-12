# Android-DodoCagePhonograph
**說明**<br>
* 達成Unity以及Android間的互相溝通
* 此為Android客戶端

**主要功能**<br>
* 藉由socket讓彼此間互相共享資訊
* 實際動作
  * 需要先將server端開啟
  * 當server端發送`%Ring`時，開始響起鈴聲
  * 按下接聽鈕後會隨機撥放留言
  * 留言結束後會傳送`&Answer`給erver並進入黑畫面
  
**DEMO**<br>
https://www.youtube.com/watch?v=RrfBGagRep0

**相關連結**<br>
Unity的server及client請參考(https://github.com/Gougon-Side-Project/Unity-SocketServerTest)
