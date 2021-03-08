package projeto.api.dtos.conformance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import projeto.api.dtos.compare.FilterInputWrapperDTO;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class CaseConformanceInputWrapperDTO extends FilterInputWrapperDTO
{
    private List<String> nodes;
}