package com.hcmute.tdshop.utils.location;

import com.google.maps.internal.StringJoin;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public class LatLng implements StringJoin.UrlValue, Serializable {

  private static final long serialVersionUID = 1L;
  public double latitude;
  public double longitude;

  public LatLng(double lat, double lng) {
    this.latitude = lat;
    this.longitude = lng;
  }

  public LatLng() {
  }

  public String toString() {
    return this.toUrlValue();
  }

  public String toUrlValue() {
    return String.format(Locale.ENGLISH, "%.8f,%.8f", this.latitude, this.longitude);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o != null && this.getClass() == o.getClass()) {
      com.google.maps.model.LatLng latLng = (com.google.maps.model.LatLng) o;
      return Double.compare(latLng.lat, this.latitude) == 0 && Double.compare(latLng.lng, this.longitude) == 0;
    } else {
      return false;
    }
  }

  public int hashCode() {
    return Objects.hash(new Object[]{this.latitude, this.longitude});
  }
}
