import org.example.AccountsSave;
import org.example.CommissionService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommissionServiceTest {
    @Mock
    AccountsSave accountsSave;

    @InjectMocks
    CommissionService commissionService;


}
