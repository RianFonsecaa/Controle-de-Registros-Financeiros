import { CobrancaQueryFilters } from "../query-filters/CobrancaQueryFilters";

export interface EnviaRelatorioWppRequest {
  filtros: CobrancaQueryFilters;
  numerosTelefone: String[];
}
