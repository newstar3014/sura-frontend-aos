package com.supercarlounge.supercar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostDriveCheckViewModel : ViewModel() {

    var titleText = MutableLiveData<String>("드라이브 등록 시 유의사항")
    var time= MutableLiveData<String>("")
    var mainText = MutableLiveData<String>("드라이브 등록 시 유의사항")
    var subText = MutableLiveData<String>("슈퍼카라운지 이용약관\n" +
            "제12조 (게시물에 대한 책임)\n" +
            "\n" +
            "1. 회사는 회원이 게시하거나 전달하는 서비스 내의 게시물이 다음 각 호의 경우에 해당한다고 판단되는 경우 사전 통지 없이 삭제할 수 있으며, 이에 대해 회사는 어떠한 책임도 지지 않습니다.\n" +
            "\n" +
            "1) 회사, 다른 회원 또는 제3자를 비방하거나 중상모략으로 명예를 손상시키는 내용인 경우\n" +
            "2) 공공질서 및 미풍양속에 위반되는 내용의 게시물에 해당하는 경우\n" +
            "3) 범죄 행위에 결부된다고 인정되는 내용인 경우\n" +
            "4) 회사 또는 제3자의 저작권, 기타 타인의 권리를 침해하는 내용인 경우\n" +
            "5) 회사의 운영진, 직원, 혹은 관계자를 사칭하여 제3자로 하여금 회사의 공식입장이라고 오인하게 하는 내용인 경우\n" +
            "6) 회사에서 제공하는 서비스와 관련 없는 내용인 경우\n" +
            "7) 불필요하거나 승인되지 않은 광고, 판촉물을 게재하는 경우\n" +
            "8) 타인의 명의 등을 무단으로 도용하여 작성한 내용이거나, 타인이 입력한 정보를 무단으로 위·변조한 내용인 경우\n" +
            "9) 동일한 내용을 중복하여 다수 게시하는 등 게시의 목적에 어긋나는 경우\n" +
            "10) 게시물의 정보를 외부 서비스에서 사용하는 행위를 금지하는 사이트에서 URL 정보를 수집하여 게재하는 경우\n" +
            "11) 기타 관계 법령 및 회사의 개별 서비스 별 세부이용지침 등에 위반된다고 판단되는 경우\n" +
            "\n" +
            "2. 회사는 개별 서비스 별로 게시물과 관련된 세부이용지침을 별도로 정하여 시행할 수 있으며, 회원은 그 지침에 따라 게시물(회원 간의 전달 포함)을 게재하여야 합니다.\n" +
            "\n" +
            "\n" +
            "제13조 (게시물의 저작권 등)\n" +
            "\n" +
            "1. 회원이 서비스 내에 게시한 게시물의 저작권은 저작권법에 의해 보호를 받으며, 회사가 작성한 저작물에 대한 저작권 및 기타 지적재산권은 회사에 귀속합니다.\n" +
            "\n" +
            "2. 회원은 자신이 서비스 내에 게시한 게시물을 회사가 국내외에서 다음 각 호의 목적으로 사용하는 것을 허락합니다.\n" +
            "\n" +
            "1) 서비스(제3자가 운영하는 사이트 또는 미디어의 일정 영역 내에 입점하여 서비스가 제공되는 경우 포함) 내에서 게시물을 사용하기 위하여 게시물의 크기를 변환하거나 단순화하는 등의 방식으로 수정하는 것\n" +
            "2) 회사 또는 관계사가 운영하는 본 서비스 및 연동 서비스 에 게시물을 복제·전송·전시하는 것. 다만, 회원이 게시물의 복제·전송·전시에 반대 의견을 전자우편을 통해 관리자에게 통지할 경우에는 그러하지 않습니다.\n" +
            "3) 회사의 서비스를 홍보하기 위한 목적으로 미디어, 소셜 미디어를 포함한 디지털 마케팅 채널, 통신사 등에게 게시물의 내용을 보도, 방영하게 하는 것\n" +
            "\n" +
            "3. 전 항의 규정에도 불구하고, 회사가 게시물을 전항 각 호에 기재된 목적 이외에 제3자에게 게시물을 제공하고 금전적 대가를 지급받는 경우에는 사전에 전화, 전자우편 등의 방법으로 회원의 동의를 얻습니다. 이 경우 회사는 회원에게 별도의 보상을 제공합니다.\n" +
            "\n" +
            "4. 회원이 서비스에 게시물을 게재하는 것은 다른 회원이 게시물을 서비스 내에서 사용하거나, 회사가 검색결과로 사용하는 것을 허락한 것으로 봅니다. 그리고 스마트폰, 태블릿 PC의 서비스 이용자(앱 또는 브라우저로 서비스를 비가입 방문한 경우도 포함)가 소프트웨어(예:  앱, 브라우저) 또는 하드웨어(예: 스마트폰, 태블릿PC)에서 제공하는 기능을 이용하여 게시물을 저장한 후 활용하는 것을 허락한 것으로 봅니다.\n" +
            "\n" +
            "5. 제20조에 의해 이용계약이 해지되는 경우 작성했던 게시물 및 댓글은 자동으로 삭제되지 않으며, 회원정보 삭제로 인해 작성자 본인을 확인할 수 없으므로 게시물 편집 및 삭제 처리가 원천적으로 불가능 합니다. 게시물 삭제를 원하시는 경우에는 먼저 해당 게시물을 삭제 하신 후, 이용계약 해지 신청(탈퇴 신청)을 하여야 합니다. 다만, 다른 회원 또는 제3자에게 의하여 스크랩, 공유 등의 기능을 통해 다시 게시된 게시물 및 댓글 등 다른 회원의 정상적인 서비스 이용에 필요한 게시물은 삭제되지 않습니다.\n" +
            "\n" +
            "6. 회사는 서비스 운영정책상 또는 회사가 운영하는 사이트 간의 통합 등을 하는 경우 게시물의 내용을 변경하지 아니하고 게시물의 게재 위치를 변경·이전하거나 사이트 간 공유하여 서비스할 수 있습니다. 다만, 게시물의 이전·변경 또는 공유를 하는 경우에는 사전에 공지합니다.")
    var checked1 = MutableLiveData<Boolean>(false)
    var checked2 = MutableLiveData<Boolean>(false)

    var isnext = MutableLiveData<Boolean>(false)
    var istoast = MutableLiveData<String>("")
    var isstart = MutableLiveData<Boolean> (false)

    fun setnext(){
        isnext.value =true
    }

    fun onChecked1(){
        checked1.value = !checked1.value!!
    }
    fun onChecked2(){
        checked2.value = !checked2.value!!
    }
}