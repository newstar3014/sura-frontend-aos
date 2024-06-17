package com.supercarlounge.supercar

import FinishDialog

class Constans {

    companion object {
        const val BRIDGE_MOVE_NOTICE = 1
        const val MYPAGEMOVE = 2
        const val FinishDialog_OK = 1
        const val FinishDialog_CANCEL = 2
        const val FinishDialog_BANNER = 3


        const val MULTI_VIEW_ADAPTER_TOP_BANNER = 0
        const val MULTI_VIEW_ADAPTER_SUGESST_HEADER= 1
        const val MULTI_VIEW_ADAPTER_SUGESST_RECYCLERVIEW= 2
        const val MULTI_VIEW_ADAPTER_VIP_DRIVE_HEADER= 3
        const val MULTI_VIEW_ADAPTER_VIP_DRIVE_RECYCLERVIEW= 4
        const val MULTI_VIEW_ADAPTER_MY_PICK_HEADER= 6
        const val MULTI_VIEW_ADAPTER_MY_PICK_RECYCLERVIEW= 7
        const val MULTI_VIEW_ADAPTER_BOTTOM_BANNER = 8
        const val MULTI_VIEW_ADAPTER_DRIVE_AWAY_HEADER= 9
        const val MULTI_VIEW_ADAPTER_DRIVE_AWAY_RECYCLERVIEW= 10


        const val JOIN_CODE = 0

        const val DIALOG_PROFILE_EDIT = 1
        const val DIALOG_PROFILE_BASE = 2
        const val DIALOG_PROFILE_BASE_2 = 3

        //프로필카드전달
        const val DIALOG_PROFILE_BASE_3 = 13

        //다이얼로그 VIP 노출 시작
        const val DIALOG_DRIVE_EXPOSURE_1 = 4

        //다이얼로그 VIP 노출 제외
        const val DIALOG_DRIVE_EXPOSURE_2 = 5

        //게시글 등록 로딩
        const val DIALOG_LOADING = 6

        //게시글 드라이브글 차단
        const val DIALOG_DRIVE_BLOCK = 7

        //게시글 삭제
        const val DIALOG_POST_DELETE = 121

        //게시글 삭제 api call
        const val DIALOG_POST_DELETE_API_CALL = 111

        //게시글 삭제 api call
        const val DIALOG_POST_DELETE_API_CALL_CLEAR = 1111

        //댓글 삭제
        const val DIALOG_POST_COMMENT_DELETE = 122

        //댓글 삭제 api call
        const val DIALOG_POST_COMMENT_DELETE_API_CALL = 112



        //대댓글 삭제
        const val DIALOG_POST_SUB_COMMENT_DELETE = 123

        //대댓글 삭제 api call
        const val DIALOG_POST_SUB_COMMENT_DELETE_API_CALL = 113

        //게시글 삭제 api call
        const val DIALOG_POST_NOTI_API_CALL = 124


        const val MY_CAR_POST_LIKE = 135
        const val MY_CAR_POST_BOOKMARK = 136
        //게시글 벤 api call
        const val DIALOG_POST_BLOCK_API_CALL = 125

        //게시글 댓글 벤 api call
        const val DIALOG_POST_BLOCK_COMMENT_API_CALL = 1251
        //게시글 대댓글 벤 api call
        const val DIALOG_POST_BLOCK_SUB_COMMENT_API_CALL = 1252
        //게시글 벤 확인 check
        const val DIALOG_POST_BLOCK_CHECK = 126


        //글 차단 api call
        const val DIALOG_POST_BLOCK_CALL = 127

        //댓글 차단 api call
        const val DIALOG_COMMENT_BLOCK_CALL = 128

        //대댓글 차단 api call
        const val DIALOG_COMMENT_SUB_BLOCK_CALL = 130

        //글 신고 api call
        const val DIALOG_POST_POLICE_CALL = 132
        //댓글 신고 api call
        const val DIALOG_COMMENT_NOTI_CALL = 133
        //대댓글 신고 api call
        const val DIALOG_COMMENT_SUB_NOTI_CALL = 131





        //게시글 삭제
        const val DIALOG_IMAGE_DELETE = 541

        //프로필 차단
        const val DIALOG_PROFILE_BLOCK = 8

        //게시글 차단
        const val DIALOG_POST_BLOCK = 9
        //게시글 확인
        const val DIALOG_POST_BLOCK_OK = 99

        // 최상위 멤버 안내창
        const val DIALOG_SPECIAL = 10

        // 보험 정보
        const val DIALOG_INSURANCE = 11

        // 서비스 준비
        const val DIALOG_SERVICE_PREPARATION = 12

        // 대표 차량
        const val DIALOG_MAIN_CAR = 13

        // 차량 정보 등록
        const val DIALOG_CAR_REGISTRATION = 14

        // 약관동의
        const val DIALOG_TERMS = 15

        // 게시글카테고리
        const val DIALOG_POST_CATEGPRY = 16

        // 드라이브 패스
        const val DIALOG_PASS_BUY = 17

        // 3점으로 평가
        const val DIALOG_EVALUATION = 18

        // 드라이브 점수 리셋
        const val DIALOG_DRIVE_RESET = 19

        // 프로필 사진 변경
        const val DIALOG_PROFILE_CHANGE = 20

        // 내 게시글 호감 대납
        const val DIALOG_CRUSH_ARRIVAL_PAYMENT = 21

        // 내 게시글 호감
        const val DIALOG_CRUSH_ARRIVAL = 22

        // 연락처 공개하기
        const val DIALOG_CONTACT_OPEN = 23

        // 연락처 공개됨
        const val DIALOG_CONTACT_OPEN_COMPLETION = 24

        // 상대방이 호감수락
        const val DIALOG_CRUSH_ACCEPT = 25

        // 호감 발송1
        const val DIALOG_CRUSH_SEND1 = 26

        // 호감 발송2
        const val DIALOG_CRUSH_SEND2 = 27

        // 호감 수락여부
        const val DIALOG_CRUSH_TRY_ACCEPT = 28

        // 하트 부족
        const val DIALOG_LACK_HEART = 29

        //프로필 열람 승인 체크
        const val DIALOG_BROWSE_CHECK = 30

        //프로필 열람 승인 요청 멘트 대납
        const val DIALOG_BROWSE_REQUEST_PAYMENT = 31

        //프로필 열람 승인 요청 멘트 기본
        const val DIALOG_BROWSE_REQUEST = 32

        // 신고가 정상적으로 접수되었을때
        const val DIALOG_POST_REPORT = 33

        const val DIALOG_COMMENT_REPORT = 34


        //PostWritingActivity
        const val REQ_GALLERY = 799

        //PostWritingActivity
        const val GRID_ADAPTER = 0

        //PostWritingActivity
        const val LINEAR_ADAPTER = 1

        //잠금 해제
        const val LOCK_CLEAR = 30

        //잠금 설정
        const val LOCK_SETTING = 31

        const val DIALOG_OK = 9999

        const val DIALOG_CANCEL = 9998

        //AnonymousPostItemNickname
        const val NICKNAME = 1
        const val HEART = 2

        //AnonymousPostItem
        const val MY_SEQ = 1
        const val YOU_SEQ = 2

        const val COMMENT_MORE = 101

        const val COMMENT_MORE_DELETE = 1001
        const val COMMENT_MORE_EDIT = 1002
        const val COMMENT_MORE_NOTI = 1003
        const val COMMENT_MORE_BLOCK = 1004

        const val COMMENT_SUB_MORE = 102


        const val COMMENT_SUB_MORE_DELETE = 2001
        const val COMMENT_SUB_MORE_EDIT = 2002
        const val COMMENT_SUB_MORE_NOTI = 2003
        const val COMMENT_SUB_MORE_BLOCK =2004
        //게시글 옵션메뉴 삭제 선택시
        const val POST_MORE_DELETE = 3001
        //게시글 옵션메뉴 수정 선택시
        const val POST_MORE_EDIT = 3002
        //게시글 옵션메뉴 신고 선택시
        const val POST_MORE_NOTI = 3003
        //게시글 옵션메뉴 차단 선택시
        const val POST_MORE_BLOCK = 3004

        const val POST_MORE = 103
        const val ORDER_MORE = 104
        const val ORDER_MORE_NEW = 105
        const val ORDER_MORE_VIEWS = 106
        const val ORDER_MORE_COMMENT = 107

        const val ORDER_MORE_ALL = 105
        const val ORDER_MORE_SAVE = 106
        const val ORDER_MORE_DEDUCTION = 107
        //스팟 정렬
        const val ORDER_MORE_REC = 105
        const val ORDER_MORE_HOT = 106
        const val ORDER_MORE_NEWSET = 107

        //홈프래그먼트 메인어뎁터 뷰타입
        const val VIEW_HOME_BANNER1 = 1
        const val VIEW_SUGGESTION_DRIVE = 2
        const val VIEW_VIP_DRIVE = 3
        const val VIEW_HOME_BANNER2 = 4
        const val VIEW_AWAY_DRIVE= 5

        //SubscriptionApplicationComplete 상태
        const val CODE_CHECK_SUCCESS = 3 // 사용할수 있는 코드가 맞는다면
        const val CODE_CHECK_FAIL = 4   // 사용할수 있는 코드가 맞지않는다면
        const val CODE_CHECK_NULL = 5 // 코드내용이 아무것도없을경우
        const val CODE_LENGTH_6 = 2 // 코드내용이 6이하일경우
        const val CODE_READY = 1  // 코드 양식이 준비가 다 된경우
        const val NON = 0
        const val CODE_CHECK_BLANK = 6 // 코드내용이 아무것도없을경우
        //Login 상태

        const val PHONE_NUMBER_SUCCESS = 10 //폰번호가 정상적으로 적힌경우
        const val PHONE_NUMBER_FAIL  = 1 //폰번호가 양식이 비정상적으로 적힌경우
        const val PHONE_NUMBER_CALL_SUCCESS = 4 //폰번호가 정상적으로 적힌경우
        const val PHONE_NUMBER_CALL_FAIL  = 5 //폰번호가 양식이 비정상적으로 적힌경우
        const val PHONE_NUMBER_API_CALL_SUCCESS  = 6 //sms 인증 api 성공
        const val PHONE_NUMBER_API_CALL_FAIL = 5 //sms 인증 api 실패
        const val PHONE_NUMBER_6 = 2 //폰번호가 6개 이하일경우
        const val PHONE_NUMBER_NULL  =  3 // 폰번호가 없을경우
        const val CERTIFICATION_NUMBER_TIME_END = 12 // 인증번호 시간이 끝남
        const val CERTIFICATION_NUMBER_TIME_START = 13   // 인증번호 시간 시작
        const val CERTIFICATION_NUMBER_NULL = 14 // 인증번호코드가 없을경우
        const val CERTIFICATION_NUMBER_6 = 15 // 인증번호 코드텍스트가 6개이하일경우
        const val CERTIFICATION_NUMBER_SUCCESS = 20 // 인증번호가 맞다면
        const val TERMS_TURE  =  21  // 약관을 충족한경우
        const val TERMS_FALSE = 22 //약관을 충족못한경우

        const val PHONE_SMS =0
        const val PHONE_PASS =1002
        const val PHONE_LOGIN =1003
        const val PHONE_WAITING =1004
        const val PHONE_COMPANION =1005
        const val PHONE_POLICE=1006
        const val PHONE_SUCCESS=1007
        const val PHONE_Q=1008
        const val PHONE_ADMIN_LOGIN =1009
        const val PHONE_ADMIN_SUCCESS=10010
        const val INVITATION_CODE_SUCCESS=11313
    }
}