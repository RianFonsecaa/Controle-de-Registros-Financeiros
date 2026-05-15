import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { environment } from "../../../enviroments/enviroments";
import { EnviaRelatorioWppRequest } from "../../model/requests/EnviaRelatorioWppRequest";

@Injectable({
  providedIn: "root",
})
export class WhatsappService {
  private readonly BASE_URL = `${environment.apiUrl}/api/whatsapp`;
  private http = inject(HttpClient);

  enviaRelatorio(requestData: EnviaRelatorioWppRequest) {
    return this.http.post(
      `${this.BASE_URL}/envia-relatorio-lista`,
      requestData,
      {
        responseType: "text",
      },
    );
  }
}
