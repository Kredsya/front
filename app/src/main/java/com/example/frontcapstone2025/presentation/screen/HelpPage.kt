import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpPage(
    navigationBack: () -> Unit,
) {
    Scaffold(
        containerColor = Color.DarkGray,
        topBar = {
            TopAppBar(
                title = { Text("도움말", color = Color.White, fontSize = 24.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                ),
                actions = {
                    IconButton(onClick = navigationBack) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Default.Close,
                            contentDescription = "Back",
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp), // 내용 여백
                verticalArrangement = Arrangement.Top
            ) {
                // 취지
                Text("취지", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "저희 앱은 자체 인터넷으로 영상을 전송하는 몰래 카메라를 탐지하기 위한 앱이고, 아래와 같은 서비스를 제공합니다.\n" +
                            "1) IP 카메라의 유무\n" +
                            "2) IP 카메라의 정확한 위치 찾기",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))


                // 사용법
                Text("사용법", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "1. 홈에서 수상한 기기를 검색하세요.\n" +
                            "2. 수상한 기기 찾기 버튼을 눌러 고정하고, 하단 왼쪽 메뉴를 통해 정확한 위치를 찾으세요.",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))


                // 동작원리
                Text("동작 원리", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        append(
                            "1. 수상한 기기 검색\n- 검색되는 wifi 중 카메라 영상을 보내는 기기를 머신러닝을 통해 찾습니다.\n\n" +
                                    "2. 기기의 정확한 위치 찾기\n- 가까운 거리에 수상한 기기가 존재할 경우, 삼변측량을 통해 사용자의 왼쪽 어깨를 기준으로 정확한 위치 정보를 알려줍니다."
                        )
                    },
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 권한 설명
                Text("권한", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        append(
                            "다음 권한들을 허용하고, 설정에서 변경해주어야 원활한 앱 사용이 가능합니다.\n\n" +
                                    "1. 저장 공간 권한\n 랜카드를 통해 얻은 네트워크 정보를 전달받기 위해 저장 공간 접근 권한이 필요합니다.\n\n" +
                                    "2. 위치 권한\n 와이파이 스캔을 하기 위해 위치 권한의 허용이 필요합니다.\n\n" +
                                    "3. wifi 스캔 쓰로틀링(=wifi 검색 제한)\n 핸드폰의 설정 > 개발자 모드 > wifi 스캔 쓰로틀링(wifi 검색 제한) 을 꺼주시면, 더 정확한 거리 탐지가 가능해집니다\n 개발자 모드 켜는 법 : 설정 > 휴대전화 정보 > 소프트웨어 정보 > 빌드번호 언속으로 클릭(해제될때까지 클릭)"
                        )
                    },
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}
