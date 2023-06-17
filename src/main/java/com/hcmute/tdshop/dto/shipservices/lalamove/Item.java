package com.hcmute.tdshop.dto.shipservices.lalamove;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
  String quantity = "0";
  String weight = "";
  List<String> categories = new ArrayList<>();

  public void setQuantity(int quantity) {
    this.quantity = String.valueOf(quantity);
  }

  public int getQuantity() {
    return Integer.parseInt(quantity);
  }
}
