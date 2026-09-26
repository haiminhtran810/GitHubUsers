# Certificate Pinning khi gọi API

## 1. Certificate pinning là gì?

Ứng dụng giao tiếp với API qua TLS (HTTPS). TLS cung cấp ba thuộc tính bảo mật chính:

1. **Bí mật (Confidentiality):** dữ liệu được mã hóa trong quá trình truyền.
2. **Toàn vẹn (Integrity):** dữ liệu không thể bị sửa đổi mà không bị phát hiện.
3. **Xác thực (Authentication):** client có thể xác minh mình đang kết nối đến đúng máy chủ.

Theo cơ chế TLS thông thường, Android tin tưởng các chứng chỉ được cấp bởi Certificate Authority (CA) có trong trust store của thiết bị. Certificate pinning bổ sung thêm một điều kiện: ngoài việc chứng chỉ phải hợp lệ theo TLS, public key trong chuỗi chứng chỉ còn phải khớp với ít nhất một pin đã được đóng gói trong ứng dụng.

Project sử dụng `okhttp3.CertificatePinner` để kiểm tra pin trước khi cho phép request tiếp tục.

## 2. Pin đang lưu giá trị gì?

Pin của OkHttp là SHA-256 hash của **Subject Public Key Info (SPKI)**, sau đó được mã hóa Base64. Nó không phải là:

- API key hoặc access token;
- private key của máy chủ;
- SHA-256 fingerprint của toàn bộ certificate;
- nội dung file `.crt` được mã hóa lại;
- một chuỗi SHA-256 dạng hexadecimal.

Định dạng hoàn chỉnh mà `CertificatePinner` nhận là:

```text
sha256/<SPKI-SHA256-ở-dạng-Base64>
```

Ví dụ đúng về mặt định dạng:

```text
sha256/S2LUIbq4yUg5w+MYbj5LZOWAZAzaeNGJ9rTTc4GjvBQ=
```

SHA-256 tạo ra 32 byte. Khi mã hóa Base64, phần hash thường dài 44 ký tự và kết thúc bằng `=`. Không dùng chuỗi hex dài 64 ký tự như `4b62d421...`.

### Quy ước riêng của project

Trong project này, `AppSecrets.certificatePinnerKey` chỉ lưu **phần Base64**, ví dụ:

```text
S2LUIbq4yUg5w+MYbj5LZOWAZAzaeNGJ9rTTc4GjvBQ=
```

Không lưu tiền tố `sha256/` vào `certificatePinnerKey`, vì `NetworkModule` đã ghép tiền tố:

```kotlin
"sha256/" + appSecrets.certificatePinnerKey
```

Nếu giá trị trong `AppSecrets` cũng có tiền tố, kết quả sẽ thành `sha256/sha256/...` và OkHttp sẽ từ chối pin.

Giá trị Base64 được XOR với khóa `0x5A` và lưu dưới dạng mảng byte trong `core/security/src/main/cpp/secrets.cpp`. Đây chỉ là biện pháp làm khó việc đọc chuỗi bằng công cụ phân tích tĩnh; nó không biến pin thành bí mật và không chống được reverse engineering hoàn toàn.

## 3. Certificate pinning hỗ trợ việc gọi API như thế nào?

Khi gọi `https://api.github.com`, luồng kiểm tra diễn ra như sau:

1. Android/OkHttp thực hiện TLS handshake và kiểm tra chứng chỉ theo trust store thông thường.
2. OkHttp lấy public key của các chứng chỉ trong chuỗi chứng chỉ máy chủ.
3. OkHttp tính SPKI SHA-256 và so sánh với các pin được cấu hình cho hostname `api.github.com`.
4. Nếu ít nhất một pin khớp, TLS handshake được chấp nhận và request API tiếp tục.
5. Nếu không có pin nào khớp, request dừng với `SSLPeerUnverifiedException: Certificate pinning failure` trước khi dữ liệu API được gửi hoặc nhận.

Certificate pinning giúp giảm nguy cơ tấn công man-in-the-middle trong trường hợp thiết bị tin nhầm một CA, người dùng cài CA không đáng tin, hoặc proxy có khả năng giải mã HTTPS. Kẻ tấn công vẫn không thể giả mạo máy chủ nếu không có public key tương ứng với pin mà ứng dụng chấp nhận.

Certificate pinning **không** thay thế:

- HTTPS và kiểm tra chứng chỉ TLS mặc định;
- xác thực người dùng bằng token;
- bảo vệ token trong thiết bị;
- kiểm tra quyền truy cập ở backend;
- mã hóa dữ liệu lưu trên máy;
- các biện pháp chống root, hook hoặc reverse engineering.

## 4. Cách lấy pin mới

Chỉ lấy pin từ mạng và máy tính đáng tin cậy. Không lấy pin trong khi đang bật proxy giải mã HTTPS như Charles, Fiddler hoặc Burp Suite, vì có thể vô tình pin public key của proxy.

### Cách 1: Lấy trực tiếp từ máy chủ

```bash
openssl s_client -servername api.github.com -connect api.github.com:443 </dev/null 2>/dev/null \
  | openssl x509 -pubkey -noout \
  | openssl pkey -pubin -outform DER \
  | openssl dgst -sha256 -binary \
  | openssl base64
```

`-servername api.github.com` rất quan trọng vì nó gửi TLS SNI, giúp máy chủ trả về đúng chứng chỉ cho hostname.

### Cách 2: Lấy từ file certificate

Nếu đã có certificate PEM đáng tin cậy, ví dụ `github.crt`:

```bash
openssl x509 -in github.crt -pubkey -noout \
  | openssl pkey -pubin -outform DER \
  | openssl dgst -sha256 -binary \
  | openssl base64
```

Kết quả của hai lệnh trên chỉ là phần Base64. Khi kiểm tra thủ công theo định dạng OkHttp, thêm `sha256/` ở phía trước. Khi đưa vào `AppSecrets` của project này thì giữ nguyên Base64, không thêm tiền tố.

Có thể kiểm tra thông tin và thời hạn certificate bằng:

```bash
openssl x509 -in github.crt -noout -subject -issuer -dates
```

## 5. Cập nhật mảng XOR trong native code

Sau khi có pin Base64 mới, tạo lại mảng byte XOR bằng:

```bash
python3 -c 's="PIN_BASE64_MOI"; print(", ".join(f"0x{ord(c)^0x5A:02X}" for c in s))'
```

Thay `PIN_BASE64_MOI` bằng kết quả Base64 thật, không kèm `sha256/`. Copy mảng được tạo ra vào biến `obfuscatedKey` trong:

```text
core/security/src/main/cpp/secrets.cpp
```

Các điểm phải kiểm tra sau khi cập nhật:

- `xorKey` khi encode và decode đều là `0x5A`;
- `AppSecretsImpl` thực sự trả về kết quả của `retrieveCertificatePinnerKey()`;
- không có chuỗi hard-code khác ghi đè lên kết quả native;
- pin hoàn chỉnh truyền cho OkHttp chỉ có đúng một tiền tố `sha256/`;
- hostname truyền vào `CertificatePinner.Builder.add()` là `api.github.com`, không phải URL đầy đủ `https://api.github.com/`.

## 6. Khi certificate hoặc public key thay đổi

Certificate có thời hạn và máy chủ có thể luân chuyển certificate/public key. Nếu public key mới không khớp với pin trong ứng dụng, mọi request đến hostname được pin sẽ thất bại dù certificate mới hoàn toàn hợp lệ theo TLS.

Pin không phải secret nên không có thao tác “reset key” trên GitHub như reset mật khẩu. Quy trình thay pin là:

1. Xác minh hostname, certificate mới và nguyên nhân thay đổi.
2. Lấy SPKI SHA-256 Base64 mới từ nguồn đáng tin cậy.
3. Tạo lại mảng XOR và cập nhật native code.
4. Build sạch để tránh dùng thư viện native cũ.
5. Kiểm thử API trên thiết bị/emulator mà không dùng proxy giải mã HTTPS.
6. Phát hành phiên bản ứng dụng mới.

Build sạch có thể thực hiện bằng:

```bash
./gradlew clean
./gradlew :app:assembleDevDebug
```

Nếu Android Studio vẫn đóng gói native library cũ sau khi copy project, đóng IDE, xóa các thư mục build được sinh tự động như `core/security/.cxx`, `core/security/.externalNativeBuild` và `core/security/build`, sau đó Sync và build lại. Không xóa source code hoặc file cấu hình CMake.

## 7. Nếu mất pin cũ thì sao?

Do pin là hash một chiều, không thể khôi phục public key hoặc certificate gốc từ pin. Tuy nhiên, không cần pin cũ để tạo pin mới:

- Nếu máy chủ vẫn hoạt động, lấy lại pin từ certificate hiện tại bằng lệnh OpenSSL ở trên.
- Nếu có file certificate đáng tin cậy, tạo lại pin từ file đó.
- Nếu máy chủ đã đổi key, lấy pin của certificate/key mới và phát hành ứng dụng mới.

Với kiến trúc hiện tại, pin được đóng gói trong APK nên không thể sửa pin từ xa cho phiên bản đã cài. Nếu phiên bản production chỉ có một pin và máy chủ đổi public key đột ngột, phiên bản đó sẽ mất khả năng gọi API; cách khôi phục là phát hành bản cập nhật ứng dụng có pin mới.

## 8. Chiến lược rotation an toàn

Không nên chỉ pin một leaf certificate duy nhất trong ứng dụng production. Leaf certificate thường có thời hạn ngắn và có thể được thay mới thường xuyên.

Nên cân nhắc:

- cấu hình ít nhất một pin đang dùng và một backup pin đã được xác minh;
- triển khai backup pin trong một bản phát hành trước khi máy chủ đổi key;
- chỉ xóa pin cũ sau khi phần lớn người dùng đã cập nhật ứng dụng;
- theo dõi ngày hết hạn và kế hoạch rotation của certificate;
- cân nhắc pin intermediate public key nếu phù hợp với mô hình vận hành và chính sách bảo mật của hệ thống.

OkHttp chấp nhận kết nối khi **ít nhất một** pin cấu hình cho hostname khớp với một certificate trong chuỗi. Muốn dùng active pin và backup pin, cần truyền nhiều pin vào cùng cấu hình `CertificatePinner.Builder.add()`. Project hiện chỉ truyền một pin, vì vậy muốn áp dụng rotation không gián đoạn thì phần cấu hình này cần được mở rộng.

Pin intermediate thường ổn định hơn leaf pin nhưng mở rộng phạm vi tin cậy cho các certificate được intermediate đó ký. Lựa chọn pin leaf hay intermediate cần cân bằng giữa mức cô lập bảo mật và khả năng vận hành/rotation.

## 9. Lỗi thường gặp

### `Invalid pin hash`

Nguyên nhân thường gặp:

- dùng hash dạng hex thay vì Base64;
- có hai tiền tố `sha256/`;
- Base64 bị thiếu ký tự hoặc mất dấu `=` ở cuối;
- copy kèm khoảng trắng hoặc ký tự xuống dòng.

### `Certificate pinning failure`

Nguyên nhân thường gặp:

- certificate/public key của máy chủ đã thay đổi;
- pin được lấy từ nhầm hostname;
- pin được lấy qua proxy giải mã HTTPS;
- ứng dụng vẫn đóng gói native library cũ;
- pin leaf đã hết hiệu lực sau khi certificate được luân chuyển.

### Chạy unit test JVM nhận chuỗi rỗng

Local JVM unit test thường không load được Android native library `security_secrets`, nên `UnsatisfiedLinkError` có thể xảy ra. Việc kiểm tra JNI/native key nên được thực hiện bằng instrumented test trên thiết bị hoặc emulator, hoặc tách phần decode thành logic có thể test độc lập.

## 10. Checklist trước khi phát hành

- [ ] Pin được lấy từ đúng hostname và nguồn đáng tin cậy.
- [ ] Hash là SPKI SHA-256 ở dạng Base64, không phải certificate fingerprint dạng hex.
- [ ] `certificatePinnerKey` không chứa `sha256/` theo quy ước của project.
- [ ] Giá trị cuối cùng truyền vào OkHttp có dạng `sha256/<Base64>`.
- [ ] Native code decode ra đúng pin mong đợi.
- [ ] Không còn giá trị hard-code ghi đè kết quả JNI.
- [ ] Đã clean và rebuild toàn bộ native library.
- [ ] Đã thử gọi API thành công trên thiết bị/emulator.
- [ ] Đã thử với pin sai và xác nhận request bị chặn.
- [ ] Đã có kế hoạch backup pin và rotation trước ngày hết hạn.
