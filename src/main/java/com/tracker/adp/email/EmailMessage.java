package com.tracker.adp.email;

public class EmailMessage {

    public static String messageToAdmin="""
        <html>
            <body>
                <span>You have new Product request<br></span>
                <span>Product Name: %s<br></span>
                <span>Description: %s<br></span>
                <span>Rent From: %s<br></span>
                <span>Rent To: %s<br></span>
                <span>Email: %s<br></span>
                <span>Phone: %s<br></span>
            </body>
        </html>""";

    public static String messageToUser="""
         <html>
             <body>
                 <div>
                    <span>Dear %s,<br/><br/></span>
                    <span>Thank you for reaching out to RentingStuffs with your wishlist of outdoor gear items. We appreciate your interest in our services and your trust in us to fulfill your equipment needs for your upcoming adventures. We have received your wishlist.</span>
                    <span><br/>Here are the details of your wishlist:</span>
                    <ul>
                        <li>Wishlist Items: <b style="font-style: italic;">%s</b></li>
                        <li>Pickup Date: <b style="font-style: italic;">%s</b></li>
                        <li>Return Date: <b style="font-style: italic;">%s</b></li>
                    </ul>
                    <span>Rest assured, we will do our best to locate the items on your wishlist and notify you as soon as they become available for rent. In the meantime, if you have any specific preferences or additional information you would like to share with us regarding your wishlist, please feel free to reach out to us at <b style="font-style: italic;">contact@rentingstuffs.com</b>.</span>
                    <span><br/><br/>We value your business and look forward to assisting you in making your outdoor adventures memorable and enjoyable. Thank you for choosing RentingStuffs, and we appreciate your patience as we work to fulfill your wishlist request.</span>
                    <div>
                        <br/><br/>Best regards,
                        <br/>RentingStuffs Team
                    </div>
                 </div>
          
             </body>
         </html>""";

    public static String orderConfirmationMessage="""
         <html>
             <body>
                 <div>
                    <span>Dear %s,<br/><br/></span>
                    <span>Thank you for renting at RentingStuffs. We have received your request and your order tracking number is <b>%s</b> </span>
                     <span><br/>You can track your order through <a style="font-style: italic;" href="%s">%s</a><br/></span>
                    <span><br/>Your order details are below:<br/><br/></span>
                    <div>
                        %s
                    </div>
                    <br/><br/>
                    <span>We will contact with you soon. Please feel free to reach out to us at <b style="font-style: italic;">contact@rentingstuffs.com</b>.</span>
                    <div>
                        <br/><br/>Best regards,
                        <br/>RentingStuffs Team
                    </div>
                 </div>
          
             </body>
         </html>""";

    public static String passwordReset="""
            <html>
                <body>
                    <div>
                       <span>Dear %s,<br/><br/></span>
                       <span>You have requested to reset your password.</span>
                       <span>Click the link below to change your password:</span>
                       <br/>
                       <span><a href="%s">Reset my password</a></span>
                       <br/><br/>
                       <span>Ignore this email if you do remember your password, or you have not made the request.</span>
                       <div>
                           <br/>Best regards,
                           <br/>RentingStuffs Team
                       </div>
                    </div>
             
                </body>
            </html>""";

}
